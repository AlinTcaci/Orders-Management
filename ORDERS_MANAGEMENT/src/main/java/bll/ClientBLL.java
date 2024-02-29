package bll;

import bll.validators.EmailValidator;
import bll.validators.Validator;
import dao.ClientDAO;
import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The ClientBLL class provides business logic methods for managing clients.
 */
public class ClientBLL {
    private final List<Validator<Client>> validators;
    private final ClientDAO clientDAO;
    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new EmailValidator());

        clientDAO = new ClientDAO(Client.class);
    }
    public Client findClientById(int id) {
        Client c = clientDAO.findById(id);
        if (c == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return c;
    }

    public int insertClient(Client client) {
        for (Validator<Client> v : validators) {
            v.validate(client);
        }
        return clientDAO.insert(client);
    }

    public Client deleteClient(Client client) {
        return clientDAO.delete(client);
    }

    public Client editClient(Client client) {
        for (Validator<Client> v : validators) {
            v.validate(client);
        }
        return clientDAO.edit(client);
    }

    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }
}
