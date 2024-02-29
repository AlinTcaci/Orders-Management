package bll;

import bll.validators.QuantityValidator;
import bll.validators.Validator;
import dao.OrderDAO;
import model.OrderC;

import java.util.ArrayList;
import java.util.List;

/**
 * The OrderBLL class provides business logic methods for managing orders.
 */
public class OrderBLL {
    private final List<Validator<OrderC>> validators;
    private final OrderDAO orderDAO;
    public OrderBLL() {
        validators = new ArrayList<Validator<OrderC>>();
        validators.add(new QuantityValidator());

        orderDAO = new OrderDAO(OrderC.class);
    }

    public List<OrderC> findAllOrders() {
        return orderDAO.findAll();
    }

    public int insertOrder(OrderC o) {
        for (Validator<OrderC> v : validators) {
            v.validate(o);
        }
        return orderDAO.insert(o);
    }

}
