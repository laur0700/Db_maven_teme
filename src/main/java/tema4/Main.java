import java.sql.*;
import java.util.ArrayList;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/db_school?serverTimezone=Europe/Bucharest";
        String username = "test";
        String password = "testpass";

        Connection connection = DriverManager.getConnection(connectionUrl, username, password);

        Customers customers = new Customers(connection);

        //get all customers
        get("/customers/get-all", (req, res) -> {
            customers.addAll();
            String resMessage = "";
            for(Customer c : customers.getCustomerList()){
                resMessage = resMessage + c + "\n";
            }
            res.status(200);
            return resMessage;
        });

        //get customer by id
        get("/customers/:id", (req, res) -> customers.getById(Integer.parseInt(req.params(":id"))));

        //insert new customer
        put("/customers/add/:username/:lastname/:firstname/:phone/:address/:city/:postalcode/:country", (req, res) -> {
            customers.insert(req.params(":username"),req.params(":lastname"),req.params(":firstname"),req.params(":phone"),
                    req.params(":address"),req.params(":city"),req.params(":postalcode"),req.params(":country"));

            res.status(201);
            return "New customer has been added!";
        });

        //update existing customer
        put("/customers/update/:id/:username/:lastname/:firstname/:phone/:address/:city/:postalcode/:country", (req, res) -> {
            customers.update(Integer.parseInt(req.params("id")),req.params(":username"),req.params(":lastname"),req.params(":firstname"),req.params(":phone"),
                    req.params(":address"),req.params(":city"),req.params(":postalcode"),req.params(":country"));

            res.status(201);
            return "Customer has been updated!";
        });

        //delete customer by id
        delete("/customers/delete/:id", (req, res) -> {
            customers.delete(Integer.parseInt(req.params(":id")));

            res.status(200);
            return "Customer has been deleted!";
        });

        //get all orders
        get("/orders/get-all", (req, res) -> {
            res.status(200);
            return customers.viewAllOrders();
        });

        //get order by id
        get("/orders/:id", (req, res) -> customers.getOrderById(Integer.parseInt(req.params(":id"))));

        //get all by customer id
        get("/orders/get-by-customer/:id", (req, res) -> {
            String message = "";

            for(Order o : customers.viewAllOrdersByCustomer(Integer.parseInt(req.params(":id")))){
                message = message + o + "\n";
            }

            res.status(200);
            return message;
        });

        //insert new order
        put("/orders/add/:customerid/:orderdate/:shippeddate/:status/:comments", (req, res) -> {
            customers.addOrder(Integer.parseInt(req.params(":customerid")),Date.valueOf(req.params(":orderdate")),Date.valueOf(req.params(":shippeddate")),
                    req.params(":status"),req.params(":comments"));

            res.status(201);
            return "Order has been added!";
        });

        //update order
        put("/orders/update/:id/:status", (req, res) -> {
            customers.updateOrderStatus(Integer.parseInt(req.params(":id")), req.params(":status"));

            res.status(201);
            return "Order was updated!";
        });

        //delete order
        delete("orders/delete/:id", (req, res) -> {
            customers.deleteOrder(Integer.parseInt(req.params(":id")));

            res.status(200);
            return "Order deleted!";
        });

        //get product with order id(JSON)
        get("/orders/products/:id", (req, res) -> {
            return customers.productOrdered(Integer.parseInt(req.params(":id")));
        });
    }
}
