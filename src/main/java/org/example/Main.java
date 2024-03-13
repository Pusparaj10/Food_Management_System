package org.example;

import org.example.models.ConnectDB;
import org.example.models.Items;
import org.example.models.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConnectDB connectDB = new ConnectDB();
        Connection connection = connectDB.getConnection();
        connectDB.createOrderItemsTable();

        // Check if the table exists, if not, create it
        createTableIfNotExists(connection);

        // Dummy food data
        List<Items> foodList = new ArrayList<>();
        foodList.add(new Items("Chicken Curry", 140, 10));
        foodList.add(new Items("Mutton Curry", 200, 5));
        foodList.add(new Items("Vegetable Biryani", 180, 8));
        foodList.add(new Items("Paneer Tikka", 160, 12));
        foodList.add(new Items("Fish Fry", 220, 6));
        foodList.add(new Items("Butter Chicken", 190, 9));
        foodList.add(new Items("Dal Makhani", 120, 10));
        foodList.add(new Items("Tandoori Roti", 20, 20));
        foodList.add(new Items("Chicken Biriyani", 220, 7));
        foodList.add(new Items("Pulao", 150, 10));
        // Add more dummy food items as needed

        System.out.println("Welcome to the Food Management System!");
        System.out.println("Available Food Items:");

        // Display food items
        for (int i = 0; i < foodList.size(); i++) {
            System.out.println((i + 1) + ". " + foodList.get(i).getName() + " - Price: $" + foodList.get(i).getPrice());
        }

        List<Items> itemList = new ArrayList<>();

        while (true) {
            System.out.print("Enter the number corresponding to the food item you want to order (0 to finish): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 0) {
                break;
            }

            if (choice > 0 && choice <= foodList.size()) {
                Items selectedFood = foodList.get(choice - 1);

                System.out.print("Enter quantity for " + selectedFood.getName() + ": ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                itemList.add(new Items(selectedFood.getName(), selectedFood.getPrice(), quantity));
            } else {
                System.out.println("Invalid choice! Please enter a valid number corresponding to the food item or enter 0 to finish.");
            }
        }

        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        System.out.print("Enter order date: ");
        String orderDate = scanner.nextLine();
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        // Insert order into database
        insertOrder(connection, orderId, orderDate, customerName, itemList);

        // Close database connection
        connectDB.closeConnection();

        scanner.close();
    }

    private static void createTableIfNotExists(Connection connection) {
        String sql = "CREATE TABLE IF NOT EXISTS orders (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "date VARCHAR(50)," +
                "customer_name VARCHAR(100)" +
                ")";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertOrder(Connection connection, String orderId, String orderDate, String customerName, List<Items> itemList) {
        String sql = "INSERT INTO orders (id, date, customer_name) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderId);
            preparedStatement.setString(2, orderDate);
            preparedStatement.setString(3, customerName);
            preparedStatement.executeUpdate();

            // Insert items into database
            for (Items item : itemList) {
                String itemSql = "INSERT INTO order_items (order_id, item_name, price, quantity) VALUES (?, ?, ?, ?)";
                PreparedStatement itemStatement = connection.prepareStatement(itemSql);
                itemStatement.setString(1, orderId);
                itemStatement.setString(2, item.getName());
                itemStatement.setDouble(3, item.getPrice());
                itemStatement.setInt(4, item.getQuantity());
                itemStatement.executeUpdate();
            }

            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
