package GUI;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import dao.ClientDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Bill;
import model.Client;
import model.OrderC;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class OrderForm extends JFrame {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JTable displayTable;
    private JTextField textFieldIDClient;
    private JTextField textFieldIDProduct;
    private JButton buttonClientsT;
    private JButton buttonProductsT;
    private JButton buttonOrdersT;
    private JTextField textFieldQuantity;
    private JButton buttonCreateOrder;

    public OrderForm() {
        add(contentPane);
        pack();
        setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        updateOrdersTable(); // update orders table
        buttonClientsT.setForeground(new Color(223,225,229));
        buttonOrdersT.setForeground(new Color(66,191,108));
        buttonProductsT.setForeground(new Color(223,225,229));

        buttonClientsT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClientsTable();
                buttonClientsT.setForeground(new Color(66,191,108));
                buttonOrdersT.setForeground(new Color(223,225,229));
                buttonProductsT.setForeground(new Color(223,225,229));
            }
        });

        buttonProductsT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductsTable();
                buttonClientsT.setForeground(new Color(223,225,229));
                buttonOrdersT.setForeground(new Color(223,225,229));
                buttonProductsT.setForeground(new Color(66,191,108));
            }
        });

        buttonOrdersT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrdersTable();
                buttonClientsT.setForeground(new Color(223,225,229));
                buttonOrdersT.setForeground(new Color(66,191,108));
                buttonProductsT.setForeground(new Color(223,225,229));
            }
        });

        buttonCreateOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crateOrder();
                updateOrdersTable();
                buttonClientsT.setForeground(new Color(223,225,229));
                buttonOrdersT.setForeground(new Color(66,191,108));
                buttonProductsT.setForeground(new Color(223,225,229));
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            StartForm frame = new StartForm();
                            setVisible(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void crateOrder() {
        if(!textFieldIDClient.getText().isEmpty()){
            if(!textFieldIDProduct.getText().isEmpty()){
                if(!textFieldQuantity.getText().isEmpty()){
                    try{
                        ClientBLL clientBLL = new ClientBLL();
                        Client client = clientBLL.findClientById(Integer.parseInt(textFieldIDClient.getText()));
                        ProductBLL productBLL = new ProductBLL();
                        Product product = productBLL.findProductById(Integer.parseInt((textFieldIDProduct.getText())));

                        int quantity = Integer.parseInt(textFieldQuantity.getText());
                        if(quantity > product.getStock()) {
                            throw new Exception();
                        }
                        product.setStock(product.getStock() - quantity);
                        productBLL.editProduct(product);
                        double price = product.getPrice() * quantity;

                        OrderBLL orderBLL = new OrderBLL();
                        OrderC order = new OrderC(0,client.getId(), product.getId(), quantity, price, Timestamp.valueOf(LocalDateTime.now()));
                        orderBLL.insertOrder(order);

                        textFieldIDClient.setText(null);
                        textFieldIDProduct.setText(null);
                        textFieldQuantity.setText(null);

                        Bill bill = new Bill(client.getName(), product.getName(), order.getQuantity(), order.getPrice(), order.getPurchase_date());
                        Bill.addBill(bill);

                    } catch (IndexOutOfBoundsException e){
                        JOptionPane.showMessageDialog(null, "You need to input an existing ID", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException e){
                        JOptionPane.showMessageDialog(null, "Quantity format invalid or too small", "Error", JOptionPane.ERROR_MESSAGE);
                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Stock is lower than quantity", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You need to input a PRODUCT QUANTITY", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You need to input an existing PRODUCT ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input an existing CLIENT ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateClientsTable(){
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAllClients();
        ClientDAO clientDAO = new ClientDAO(Client.class);
        clientDAO.generateTable(displayTable, clients);
    }

    public void updateProductsTable(){
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAllProducts();
        ProductDAO productDAO = new ProductDAO(Product.class);
        productDAO.generateTable(displayTable, products);
    }

    public void updateOrdersTable(){
        OrderBLL orderBLL = new OrderBLL();
        List<OrderC> orders = orderBLL.findAllOrders();
        OrderDAO orderDAO = new OrderDAO(OrderC.class);
        orderDAO.generateTable(displayTable, orders);
        displayTable.getColumnModel().getColumn(5).setPreferredWidth(170);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrderForm frame = new OrderForm();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
