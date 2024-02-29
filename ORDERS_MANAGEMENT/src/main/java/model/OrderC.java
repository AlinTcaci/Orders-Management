package model;

import java.sql.Timestamp;

/**
 * The OrderC class represents an order object with ID, ID_client, ID_product, quantity, price, and purchase date.
 */
public class OrderC {
    private int id_orders;
    private int id_client;
    private int id_product;
    private int quantity;
    private double price;
    private Timestamp purchase_date;

    public OrderC() {
    }

    public OrderC(int id_client, int id_product, int quantity, double price, Timestamp purchase_date) {
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
        this.price = price;
        this.purchase_date = purchase_date;
    }

    public OrderC(int id_orders, int id_client, int id_product, int quantity, double price, Timestamp purchase_date) {
        this.id_orders = id_orders;
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
        this.price = price;
        this.purchase_date = purchase_date;
    }

    public int getId_orders() {
        return id_orders;
    }

    public void setId_orders(int id_orders) {
        this.id_orders = id_orders;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Timestamp purchase_date) {
        this.purchase_date = purchase_date;
    }
}
