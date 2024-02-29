package dao;

import model.Client;

import javax.swing.*;
import java.util.List;

/**
 * The ClientDAO class is responsible for performing CRUD operations on Client objects in the database.
 * It extends the AbstractDAO class which provides a set of generic methods for CRUD operations.
 */
public class ClientDAO extends AbstractDAO<Client> {

    public ClientDAO(Class<Client> type) {
        super(type);
    }

    @Override
    public Client findById(int id) {
        return super.findById(id);
    }

    @Override
    public int insert(Client client) {
        return super.insert(client);
    }

    @Override
    public Client delete(Client client) {
        return super.delete(client);
    }

    @Override
    public Client edit(Client client) {
        return super.edit(client);
    }

    @Override
    public List<Client> findAll() {
        return super.findAll();
    }

    @Override
    public void generateTable(JTable table, List<Client> clients) {
        super.generateTable(table, clients);
    }
}
