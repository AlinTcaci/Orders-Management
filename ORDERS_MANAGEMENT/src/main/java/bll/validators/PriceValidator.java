package bll.validators;

import model.Product;

/**
 * The PriceValidator class is validating the price format of a product.
 */
public class PriceValidator implements Validator<Product>{
    private static final double MIN_PRICE = 0.01;

    public void validate(Product t) {

        if (t.getPrice() < MIN_PRICE) {
            throw new IllegalArgumentException("The Product Price is too low!");
        }

    }

}
