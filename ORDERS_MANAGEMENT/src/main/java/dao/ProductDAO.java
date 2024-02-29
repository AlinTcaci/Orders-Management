package dao;

import model.Product;

import javax.swing.*;
import java.util.List;

/**
 * The ProductDAO class is responsible for performing CRUD operations on Product objects in the database.
 * It extends the AbstractDAO class which provides a set of generic methods for CRUD operations.
 */
public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(Class<Product> type) {
        super(type);
    }

    @Override
    public Product findById(int id) {
        return super.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return super.findAll();
    }

    @Override
    public Product edit(Product product) {
        return super.edit(product);
    }

    @Override
    public Product delete(Product product) {
        return super.delete(product);
    }

    @Override
    public int insert(Product product) {
        return super.insert(product);
    }

    @Override
    public void generateTable(JTable table, List<Product> products) {
        super.generateTable(table, products);
    }
}
