package GUI;

import bll.ClientBLL;
import dao.ClientDAO;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientForm extends JFrame {
    private JPanel contentPane;
    private JTable clientsTable;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JTextField textFieldDeleteByID;
    private JTextField textFieldEmail;
    private JTextField textFieldEditName;
    private JTextField textFieldEditEmail;
    private JButton buttonEdit;
    private JTextField textFieldEditByID;

    public ClientForm() {
        add(contentPane);
        pack();
        setMinimumSize(new Dimension(400, 200));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        updateClientsTable(); // update table

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
                updateClientsTable();
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonDelete();
                updateClientsTable();
            }
        });

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonEdit();
                updateClientsTable();
            }
        });
    }

    private void onButtonEdit() {
        if(!textFieldEditByID.getText().isEmpty()){
            try{
                ClientBLL clientBLL = new ClientBLL();
                Client client = clientBLL.findClientById(Integer.parseInt(textFieldEditByID.getText()));
                if(!textFieldEditName.getText().isEmpty()) client.setName(textFieldEditName.getText());
                if(!textFieldEditEmail.getText().isEmpty()) client.setEmail(textFieldEditEmail.getText());
                clientBLL.editClient(client);
                textFieldEditEmail.setText(null);
                textFieldEditName.setText(null);
                textFieldEditByID.setText(null);
            } catch (IndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(null, "You need to input an existing ID", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e){
                JOptionPane.showMessageDialog(null, "Edited email is not valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input an existing ID in EDIT CLIENT - ID field", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onButtonAdd(){
        if(!textFieldName.getText().isEmpty() && !textFieldEmail.getText().isEmpty()){
            Client client = new Client(0, textFieldName.getText(), textFieldEmail.getText());
            ClientBLL clientBLL = new ClientBLL();

            try{
                clientBLL.insertClient(client);
                textFieldName.setText(null);
                textFieldEmail.setText(null);
            } catch (IllegalArgumentException e){
                JOptionPane.showMessageDialog(null, "Email is not valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input all the ADD CLIENT fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onButtonDelete() {
        if (!textFieldDeleteByID.getText().isEmpty()) {
            ClientBLL clientBLL = new ClientBLL();
            try {
                Client client = clientBLL.findClientById(Integer.parseInt(textFieldDeleteByID.getText()));
                clientBLL.deleteClient(client);
                textFieldDeleteByID.setText(null);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "You need to input an existing ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You need to input an existing ID in DELETE CLIENT field", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateClientsTable(){
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAllClients();
        ClientDAO clientDAO = new ClientDAO(Client.class);
        clientDAO.generateTable(clientsTable, clients);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientForm frame = new ClientForm();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
