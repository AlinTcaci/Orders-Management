package bll;

import bll.validators.PriceValidator;
import bll.validators.StockValidator;
import bll.validators.Validator;
import dao.ProductDAO;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The ProductBLL class provides business logic methods for managing products.
 */
public class ProductBLL {
    private final List<Validator<Product>> validators;
    private final ProductDAO productDAO;
    public ProductBLL() {
        validators = new ArrayList<Validator<Product>>();
        validators.add(new StockValidator());
        validators.add(new PriceValidator());

        productDAO = new ProductDAO(Product.class);
    }
    public Product findProductById(int id) {
        Product p = productDAO.findById(id);
        if (p == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return p;
    }

    public int insertProduct(Product p) {
        for (Validator<Product> v : validators) {
            v.validate(p);
        }
        return productDAO.insert(p);
    }

    public Product deleteProduct(Product p) {
        return productDAO.delete(p);
    }

    public Product editProduct(Product p) {
        for (Validator<Product> v : validators) {
            v.validate(p);
        }
        return productDAO.edit(p);
    }

    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }
}
