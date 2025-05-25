package com.shop.inventory.model;

import java.io.Serializable;

//Represents a product in the inventory with properties like ID, name, price, and quantity.
 
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private double price;
    private int quantity;
    
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Price: MWK%.2f, Quantity: %d", 
                id, name, price, quantity);
    }
}