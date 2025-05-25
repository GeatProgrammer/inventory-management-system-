package src.main.java.com.shop.inventory.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.shop.inventory.model.Product;


 //Represents a sales transaction with date, items sold, and total amount.
 
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDate date;
    private Map<Product, Integer> itemsSold; // Product and quantity
    private double totalAmount;
    
    public Sale() {
        this.date = LocalDate.now();
        this.itemsSold = new HashMap<>();
        this.totalAmount = 0.0;
    }
    
    public void addItem(Product product, int quantity) {
        itemsSold.put(product, quantity);
        totalAmount += product.getPrice() * quantity;
    }
    
    // Getters
    public LocalDate getDate() { return date; }
    public Map<Product, Integer> getItemsSold() { return itemsSold; }
    public double getTotalAmount() { return totalAmount; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sale Date: ").append(date.format(DateTimeFormatter.ISO_DATE)).append("\n");
        sb.append("Items Sold:\n");
        
        for (Map.Entry<Product, Integer> entry : itemsSold.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            sb.append(String.format("- %s x%d @ MWK%.2f each\n", 
                    product.getName(), quantity, product.getPrice()));
        }
        
        sb.append(String.format("Total Amount: MWK%.2f", totalAmount));
        return sb.toString();
    }
}