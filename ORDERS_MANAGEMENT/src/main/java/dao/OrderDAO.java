package dao;

import model.OrderC;

import javax.swing.*;
import java.util.List;

/**
 * The OrderDAO class is responsible for performing CRUD operations on OrderC objects in the database.
 * It extends the AbstractDAO class which provides a set of generic methods for CRUD operations.
 */
public class OrderDAO extends AbstractDAO<OrderC>{
    public OrderDAO(Class<OrderC> type) {
        super(type);
    }

    @Override
    public List<OrderC> findAll() {
        return super.findAll();
    }

    @Override
    public OrderC findById(int id) {
        return super.findById(id);
    }

    @Override
    public int insert(OrderC order) {
        return super.insert(order);
    }

    @Override
    public void generateTable(JTable table, List<OrderC> orders) {
        super.generateTable(table, orders);
    }

    @Override
    public OrderC delete(OrderC order) {
        return super.delete(order);
    }

    @Override
    public OrderC edit(OrderC order) {
        return super.edit(order);
    }
}
