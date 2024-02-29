package model;

import connection.ConnectionFactory;
import java.sql.*;

/**
 * The Bill class represents a bill object containing information about a client's purchase.
 * It includes the client's name, product name, quantity, price, and purchase date.
 */
public record Bill(String client_name, String product_name, int quantity, double price, Timestamp purchase_date) {

    /**
     * Adds a bill record to the database.
     *
     * @param bill the bill object to be added
     */
    public static void addBill(Bill bill){
        String insertStatementString = "INSERT INTO log (client_name, product_name, quantity, price, purchase_date)" + " VALUES (?,?,?,?,?)";
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, bill.client_name);
            insertStatement.setString(2, bill.product_name);
            insertStatement.setInt(3, bill.quantity);
            insertStatement.setDouble(4, bill.price);
            insertStatement.setTimestamp(5, bill.purchase_date);

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }

    }
}
