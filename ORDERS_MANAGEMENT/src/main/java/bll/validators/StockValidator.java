package bll.validators;

import model.Product;

/**
 * The StockValidator class is validating the stock format of a product.
 */
public class StockValidator implements Validator<Product> {
    private static final int MIN_STOCK = 0;

    public void validate(Product t) {
        if (t.getStock() < MIN_STOCK) {
            throw new IllegalArgumentException("The Product Stock is too low!");
        }
    }
}
