package bll.validators;

import model.OrderC;

/**
 * The QuantityValidator class is validating the quantity format of an order.
 */
public class QuantityValidator implements Validator<OrderC> {
    private static final int MIN_QUANTITY = 1;

    @Override
    public void validate(OrderC t) {
        if (t.getQuantity() < MIN_QUANTITY) {
            throw new IllegalArgumentException("The Order Quantity is too low!");
        }
    }
}
