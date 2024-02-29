package GUI;

import bll.ProductBLL;
import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProductForm extends JFrame {
    private JPanel contentPane;
    private JTextField textFieldAddName;
    private JButton buttonAdd;
    private JTextField textFieldAddStock;
    private JTextField textFieldDeleteByID;
    private JButton buttonDelete;
    private JButton buttonCancel;
    private JTable productsTable;
    private JTextField textFieldAddPrice;
    private JButton buttonEdit;
    private JTextField textFieldEditName;
    private JTextField textFieldEditPrice;
    private JTextField textFieldEditStock;
    private JTextField textFieldEditByID;

    public ProductForm() {
        add(contentPane);
        pack();
        setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        updateProductsTable(); // update table

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

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonAdd();
                updateProductsTable();
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonDelete();
                updateProductsTable();
            }
        });

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonEdit();
                updateProductsTable();
            }
        });
    }

    private void onButtonAdd(){
        if(!textFieldAddName.getText().isEmpty() && !textFieldAddPrice.getText().isEmpty() && !textFieldAddStock.getText().isEmpty()){
            try{
                Product product = new Product(0,textFieldAddName.getText(), Double.parseDouble(textFieldAddPrice.getText()), Integer.parseInt(textFieldAddStock.getText()));
                ProductBLL productBLL = new ProductBLL();
                productBLL.insertProduct(product);
                textFieldAddName.setText(null);
                textFieldAddPrice.setText(null);
                textFieldAddStock.setText(null);
            } catch (IllegalArgumentException e){
                JOptionPane.showMessageDialog(null, "Price or Stock are too low or invalid format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input all the ADD PRODUCT fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onButtonDelete() {
        if (!textFieldDeleteByID.getText().isEmpty()) {
            try {
                ProductBLL productBLL = new ProductBLL();
                Product product = productBLL.findProductById(Integer.parseInt((textFieldDeleteByID.getText())));
                productBLL.deleteProduct(product);
                textFieldDeleteByID.setText(null);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "You need to input an existing ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input an existing ID in DELETE PRODUCT field", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onButtonEdit() {
        if(!textFieldEditByID.getText().isEmpty()){
            try{
                ProductBLL productBLL = new ProductBLL();
                Product product = productBLL.findProductById(Integer.parseInt((textFieldEditByID.getText())));
                if(!textFieldEditName.getText().isEmpty()) product.setName(textFieldEditName.getText());
                if(!textFieldEditPrice.getText().isEmpty()) product.setPrice(Double.parseDouble(textFieldEditPrice.getText()));
                if(!textFieldEditStock.getText().isEmpty()) product.setStock(Integer.parseInt((textFieldEditStock.getText())));
                productBLL.editProduct(product);
                textFieldEditByID.setText(null);
                textFieldEditPrice.setText(null);
                textFieldEditStock.setText(null);
                textFieldEditName.setText(null);
            } catch (IndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(null, "You need to input an existing ID", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e){
                JOptionPane.showMessageDialog(null, "Price or Stock are too low or invalid format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input an existing ID in EDIT PRODUCT - ID field", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateProductsTable(){
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAllProducts();
        ProductDAO productDAO = new ProductDAO(Product.class);
        productDAO.generateTable(productsTable, products);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProductForm frame = new ProductForm();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
