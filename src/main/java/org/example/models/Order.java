package org.example.models;

import org.example.models.Items;

import java.util.List;

public class Order {
    String ID;
    String date;
    String customer_name;
    List<Items> itemsList;

    public Order(String ID, String date, String customer_name, List<Items> itemsList) {
        this.ID = ID;
        this.date = date;
        this.customer_name = customer_name;
        this.itemsList = itemsList;
    }

    public void getBill() {
        int total = 0;
        System.out.println("Order ID: " + this.ID);
        System.out.println("Date: " + this.date);
        System.out.println("Customer Name: " + this.customer_name);

        for (Items item : this.itemsList) {
            System.out.println("Item Name: " + item.getName());
            System.out.println("Item Price: " + item.getPrice());
            System.out.println("Item Quantity: " + item.getQuantity());

            total += item.getPrice() * item.getQuantity();
        }
        System.out.println("Total: " + total);
    }
}
