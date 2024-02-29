package dao;

import connection.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AbstractDAO class provides a generic implementation of Data Access Object (DAO) operations for a specified type.
 *
 * @param <T> the type of the objects to be managed by the DAO
 */
public class AbstractDAO<T>{
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO(Class<T> type) {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Creates a SELECT query for retrieving objects by a specific field.
     *
     * @param field the field to be used in the WHERE clause of the query
     * @return the SELECT query string
     */
    private String createSelectQuery(String field) {
        return "SELECT * FROM " + type.getSimpleName() + " WHERE " + field + " =?";
    }

    /**
     * Retrieves an object by its ID.
     *
     * @param id the ID of the object to retrieve
     * @return the retrieved object, or null if not found
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + sqlException.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Creates a list of objects from a ResultSet.
     *
     * @param resultSet the ResultSet containing the objects' data
     * @return a list of objects created from the ResultSet
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] constructors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < constructors.length; i++) {
            ctor = constructors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException |
                 InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserts an object into the database.
     *
     * @param t the object to be inserted
     * @return the ID of the inserted object, or -1 if the insertion fails
     */
    public int insert(T t) {
        Connection connection = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;
        int insertedId = -1;
        String query = createInsertQuery(type.getDeclaredFields());
        try {

            connection = ConnectionFactory.getConnection();
            insertStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(t);
                insertStatement.setObject(index, value);
                index++;
            }

            insertStatement.executeUpdate();

            resultSet = insertStatement.getGeneratedKeys();
            if (resultSet.next()) {
                insertedId = resultSet.getInt(1);
            }

        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "DAO:insert " + e.getMessage());
            return -1;
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(connection);
        }
        return insertedId;
    }

    /**
     * Creates the SQL INSERT query for the given fields.
     *
     * @param fields An array of fields for which the INSERT query is created.
     * @return The generated INSERT query as a String.
     */
    private String createInsertQuery(Field[] fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").
                append(type.getSimpleName()).
                append(" (");
        for (Field field : fields) {
            sb.append(field.getName()).
                    append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(") VALUES (");
        for (Field field : fields) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");
        return sb.toString();
    }

    /**
     * Deletes the specified object from the database.
     *
     * @param t The object to be deleted.
     * @return The deleted object if successful, or null if an exception occurs.
     */
    public T delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            Field field = type.getDeclaredFields()[0];
            field.setAccessible(true);
            statement.setInt(1, (Integer) field.get(t));
            statement.executeUpdate();

        } catch (SQLException | IllegalAccessException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + sqlException.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Updates the specified object in the database.
     *
     * @param t The object to be updated.
     * @return The updated object if successful, or null if an exception occurs.
     */
    public T edit(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createEditQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            int index = 1;
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(t);
                statement.setObject(index, value);
                index++;
            }

            statement.setInt(index, (Integer) fields[0].get(t));
            statement.executeUpdate();

        } catch (SQLException | IllegalAccessException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:edit " + sqlException.getMessage());
            return null;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Creates the SQL UPDATE query for updating an object.
     *
     * @return The generated UPDATE query as a String.
     */
    private String createEditQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").
                append(type.getSimpleName()).
                append(" SET ");
        for (Field field : type.getDeclaredFields()) {
            sb.append(field.getName()).
                    append(" = ").
                    append("?,");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" WHERE id = ?");
        return sb.toString();
    }

    /**
     * Retrieves all objects of type T from the database.
     *
     * @return A list of objects of type T if successful, or null if an exception occurs.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + sqlException.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Generates a table with the given JTable and list of objects.
     *
     * @param table  The JTable to populate with data.
     * @param tList  The list of objects to be displayed in the table.
     */
    public void generateTable(JTable table, List<T> tList) {
        DefaultTableModel tableModel = new DefaultTableModel();

        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            tableModel.addColumn(field.getName());
        }

        for (T t : tList) {
            Object[] rowData = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field field = fields[i];
                    field.setAccessible(true);
                    Object value = field.get(t);
                    rowData[i] = value;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableModel.addRow(rowData);
        }
        table.setModel(tableModel);
    }

}
