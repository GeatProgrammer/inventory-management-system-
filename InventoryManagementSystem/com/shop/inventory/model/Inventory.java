package com.shop.inventory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import src.main.java.com.shop.inventory.model.Sale;

import java.time.LocalDate;


//Manages the collection of products and sales records.
 
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Product> products;
    private List<Sale> sales;
    
    public Inventory() {
        this.products = new ArrayList<>();
        this.sales = new ArrayList<>();
    }
    
    // Product management methods
    public void addProduct(Product product) {
        products.add(product);
    }
    
    public boolean removeProduct(int productId) {
        return products.removeIf(p -> p.getId() == productId);
    }
    
    public Product findProductById(int productId) {
        return products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    // Sales management methods
    public void addSale(Sale sale) {
        sales.add(sale);
    }
    
    public List<Sale> getSalesByDate(LocalDate date) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getDate().equals(date)) {
                result.add(sale);
            }
        }
        return result;
    }
    
    public List<Sale> getAllSales() {
        return new ArrayList<>(sales);
    }

    public void removeSalesByDate(LocalDate date) {
        sales.removeIf(sale -> sale.getDate().equals(date));
    }
}