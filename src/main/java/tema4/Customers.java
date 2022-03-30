package tema4;

import java.sql.*;
import java.util.ArrayList;

public class Customers {
    private ArrayList<Customer> customers;
    private Connection dbConnection;
    private int lastOrderId;

    public Customers(Connection dbConnection) {
        this.customers = new ArrayList<>();
        this.dbConnection = dbConnection;
        this.lastOrderId = 1;
    }

    public Customer getById(int id) throws SQLException {
        Statement psr = dbConnection.createStatement();
        ResultSet rs = psr.executeQuery("SELECT * FROM customers WHERE id = " + id);

        while (rs.next()) {
            Customer newCustomer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));

            return newCustomer;
        }

        return null;
    }

    public void addById(int id) throws SQLException {
        Customer newCustomer = getById(id);

        if (customers.contains(newCustomer))
            System.out.println("Customer already exists");
        else{
            customers.add(newCustomer);
            System.out.println("Customer added");
        }
    }

    public ResultSet getAll() throws SQLException {
        Statement psr = dbConnection.createStatement();
        ResultSet rs = psr.executeQuery("SELECT * FROM customers");

        return rs;
    }

    public void addAll() throws SQLException {
        customers = new ArrayList<>();
        ResultSet rs = getAll();

        while (rs.next()) {
            customers.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
        }
    }

    public void update(int id, String username, String lastName, String firstName, String phone, String address, String city, String postalCode, String country) throws SQLException {
        PreparedStatement psw = dbConnection.prepareStatement(
                "UPDATE customers SET `username`=?, `last_name`=?, `first_name`=?, `phone`=?, `address`=?, `city`=?, `postalCode`=?, `country`=? WHERE `id` = ?"
        );
        psw.setString(1, username);
        psw.setString(2, lastName);
        psw.setString(3, firstName);
        psw.setString(4, phone);
        psw.setString(5, address);
        psw.setString(6, city);
        psw.setString(7, postalCode);
        psw.setString(8, country);
        psw.setInt(9, id);
        psw.execute();

        addAll();
    }

    public void insert(String username, String lastName, String firstName, String phone, String address, String city, String postalCode, String country) throws SQLException {
        PreparedStatement psw = dbConnection.prepareStatement(
                "INSERT INTO customers (`username`, `last_name`, `first_name`, `phone`, `address`, `city`, `postalCode`, `country`) VALUES (?,?,?,?,?,?,?,?)");
        psw.setString(1, username);
        psw.setString(2, lastName);
        psw.setString(3, firstName);
        psw.setString(4, phone);
        psw.setString(5, address);
        psw.setString(6, city);
        psw.setString(7, postalCode);
        psw.setString(8, country);
        psw.execute();

        addAll();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement psw = dbConnection.prepareStatement(
                "DELETE FROM customers WHERE id = ?"
        );

        psw.setInt(1, id);
        psw.execute();

        addAll();
    }

    public void addOrder(int customerId, Date orderDate, Date shippedDate, String status, String comments) throws SQLException{
        Statement psr = dbConnection.createStatement();
        ResultSet rs = psr.executeQuery("SELECT * FROM orders");
        rs.last();
        this.lastOrderId = rs.getInt(1) + 1;

        PreparedStatement psw = dbConnection.prepareStatement(
                "INSERT INTO orders (`id`,`order_date`, `shipped_date`, `status`, `comments`, `customer_id`) VALUES (?,?,?,?,?,?)"
        );

        psw.setInt(1, this.lastOrderId);
        psw.setDate(2, orderDate);
        psw.setDate(3, shippedDate);
        psw.setString(4, status);
        psw.setString(5, comments);
        psw.setInt(6, customerId);

        psw.execute();
        ++this.lastOrderId;

        PreparedStatement psWrite = dbConnection.prepareStatement(
                "UPDATE products SET `stock`=(`stock` - 1) WHERE code = (SELECT product_code FROM orderdetails WHERE orderdetails.id = 1);"
        );
        psWrite.execute();
    }

    public ArrayList<Order> viewAllOrdersByCustomer(int customerId) throws SQLException {
        Statement psr = dbConnection.createStatement();
        ResultSet rs = psr.executeQuery("SELECT * FROM orders WHERE customer_id = " + customerId);

        while (rs.next()) {
            Order order = new Order(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4),
                    rs.getString(5), rs.getInt(6));

            for(Customer c : customers){
                if(c.getId() == customerId){
                    c.addOrder(order);
                    break;
                }
            }
        }

        for(Customer c : customers){
            if(c.getId() == customerId){
                return c.getOrders();
            }
        }
        return null;
    }

    public void viewAllOrders() throws SQLException {
        Statement psr = dbConnection.createStatement();
        ResultSet rs = psr.executeQuery("SELECT * FROM orders");

        while (rs.next()) {
            Order order = new Order(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4),
                    rs.getString(5), rs.getInt(6));
            System.out.println(order);
        }
    }

    public void updateOrderStatus(int id, String status) throws SQLException {
        PreparedStatement psw = dbConnection.prepareStatement(
                "UPDATE orders SET `status`=? WHERE `id` = ?"
        );

        psw.setString(1, status);
        psw.setInt(2, id);

        psw.execute();
    }

    public void addCommentToOrder(int id, String comments) throws SQLException {
        PreparedStatement psw = dbConnection.prepareStatement(
                "UPDATE orders SET `comments`=? WHERE `id` = ?"
        );

        psw.setString(1, comments);
        psw.setInt(2, id);

        psw.execute();
    }

    public ArrayList<Customer> getCustomerList() {
        return this.customers;
    }
}
