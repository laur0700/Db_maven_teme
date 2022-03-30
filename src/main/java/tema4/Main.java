package tema4;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/db_name";
        String username = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(connectionUrl, username, password);

        Customers customers = new Customers(connection);

        //customers.insert("mariusica", "Voicu", "Marius", "0725291445", "Magheru 32", "Reghin", "022343", "Romania");

        customers.update(2, "lauRAAAA10000", "Zelensky", "Laura", "0725441295", "Hapi 32", "Constanta", "029843", "Romania");

        customers.addAll(); // addAll, face refresh la tot arraylist-ul, dupa un update/insert/delete este redundant pentru ca se apeleaza si in ele\

        for(Customer x : customers.getCustomerList()){
            System.out.println(x);
        }
        System.out.println("\n");

        // update este implementat doar cu toti parametrii
        customers.update(2, "lauraX12", "Marinescu", "Laura", "0725441295", "Hapi 32", "Bucuresti", "021743", "Romania");

        for(Customer x : customers.getCustomerList()){
            System.out.println(x);
        }
        System.out.println("\n");

        customers.addById(2); //clientul acesta exista dupa `id` deja in arraylist ul cu clienti, nu mai poate fi adaugat

        customers.delete(1);

        for(Customer x : customers.getCustomerList()){
            System.out.println(x);
        }
        System.out.println("\n");

        customers.addOrder(4, Date.valueOf("2021-10-13"),Date.valueOf("2021-10-15"),"nothing","nothing");
        customers.updateOrderStatus(4, "livrat");
        customers.addCommentToOrder(4, "produsul sa fie ambalat discret");

        customers.viewAllOrders();
        System.out.println("\n");
    }
}
