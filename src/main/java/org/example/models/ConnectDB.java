package org.example.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {

    private Connection connection;

    public ConnectDB() {
        String url = "jdbc:sqlite:mydb.db"; // Specify your database URL
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Error Connecting to Database");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                System.out.println("Connection Closed");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create tables if not exists
    public void createTablesIfNotExist() {
        createOrdersTable();
        createOrderItemsTable();
    }

    // Method to create orders table if not exists
    private void createOrdersTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS orders (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "date VARCHAR(50)," +
                    "customer_name VARCHAR(100)" +
                    ")";
            statement.execute(sql);
            System.out.println("Table 'orders' created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table 'orders'");
            e.printStackTrace();
        }
    }

    // Method to create order_items table if not exists
    public void createOrderItemsTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id VARCHAR(50)," +
                    "item_name VARCHAR(100)," +
                    "price DOUBLE," +
                    "quantity INTEGER," +
                    "FOREIGN KEY(order_id) REFERENCES orders(id)" +
                    ")";
            statement.execute(sql);
            System.out.println("Table 'order_items' created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table 'order_items'");
            e.printStackTrace();
        }
    }
}
