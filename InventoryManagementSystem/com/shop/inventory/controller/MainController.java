package com.shop.inventory.controller;

import com.shop.inventory.model.*;
import com.shop.inventory.util.FileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import src.main.java.com.shop.inventory.model.Sale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.IOException;

public class MainController {
    private Inventory inventory;

    // Product UI elements
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private TableColumn<Product, Integer> productQuantityColumn;

    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField productPriceField;
    @FXML private TextField productQuantityField;

    // Sales UI elements
    @FXML private TableView<Sale> salesTable;
    @FXML private DatePicker saleDatePicker;
    @FXML private ComboBox<Product> saleProductCombo;
    @FXML private TextField saleQuantityField;
    @FXML private TextArea saleDetailsArea;

    // Report UI elements
    @FXML private DatePicker reportDatePicker;
    @FXML private TextArea reportArea;

    @FXML
    public void initialize() {
        try {
            // Try to load existing inventory
            inventory = FileUtil.loadInventory();
        } catch (Exception e) {
            // If loading fails, create new inventory
            inventory = new Inventory();
        }

        // Initialize product table
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        refreshProductTable();

        // Initialize sales components
        saleProductCombo.setItems(FXCollections.observableArrayList(inventory.getAllProducts()));
        saleDatePicker.setValue(LocalDate.now());

        // Initialize report components
        reportDatePicker.setValue(LocalDate.now());
    }

    // Product management methods
    @FXML
    private void handleAddProduct() {
        try {
            int id = Integer.parseInt(productIdField.getText());
            String name = productNameField.getText();
            double price = Double.parseDouble(productPriceField.getText());
            int quantity = Integer.parseInt(productQuantityField.getText());

            if (name.isEmpty()) {
                showAlert("Error", "Product name cannot be empty");
                return;
            }

            Product product = new Product(id, name, price, quantity);
            inventory.addProduct(product);
            saveInventory();
            refreshProductTable();
            saleProductCombo.setItems(FXCollections.observableArrayList(inventory.getAllProducts()));

            // Clear fields
            productIdField.clear();
            productNameField.clear();
            productPriceField.clear();
            productQuantityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for ID, price, and quantity");
        } catch (Exception e) {
            showAlert("Error", "Failed to add product: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            inventory.removeProduct(selected.getId());
            saveInventory();
            refreshProductTable();
            saleProductCombo.setItems(FXCollections.observableArrayList(inventory.getAllProducts()));
        } else {
            showAlert("Error", "Please select a product to delete");
        }
    }

    private void refreshProductTable() {
        ObservableList<Product> productData = FXCollections.observableArrayList(inventory.getAllProducts());
        productTable.setItems(productData);
    }

    // Sales management methods
    @FXML
    private void handleAddToSale() {
        Product selected = saleProductCombo.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a product");
            return;
        }

        try {
            int quantity = Integer.parseInt(saleQuantityField.getText());
            if (quantity <= 0) {
                showAlert("Error", "Quantity must be positive");
                return;
            }

            if (quantity > selected.getQuantity()) {
                showAlert("Error", "Not enough stock available");
                return;
            }

            // Update sale details display
            String newLine = String.format("%s x%d @ MWK%.2f each = MWK%.2f\n",
                    selected.getName(), quantity, selected.getPrice(), selected.getPrice() * quantity);
            saleDetailsArea.appendText(newLine);

            // Clear quantity field
            saleQuantityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid quantity");
        }
    }

    @FXML
    private void handleCompleteSale() {
        if (saleDetailsArea.getText().isEmpty()) {
            showAlert("Error", "No items in the sale");
            return;
        }

        try {
            Sale sale = new Sale();
            String[] lines = saleDetailsArea.getText().split("\n");

            for (String line : lines) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                // Parse the line to get product and quantity
                String productName = line.substring(0, line.indexOf(" x")).trim();
                int quantity = Integer.parseInt(line.substring(line.indexOf("x") + 1, line.indexOf("@")).trim());

                // Find the product in inventory
                Product product = inventory.getAllProducts().stream()
                        .filter(p -> p.getName().equals(productName))
                        .findFirst()
                        .orElse(null);

                if (product != null) {
                    // Add to sale and update inventory
                    sale.addItem(product, quantity);
                    product.setQuantity(product.getQuantity() - quantity);
                }
            }

            // Record the sale
            inventory.addSale(sale);
            saveInventory();
            refreshProductTable();

            // Clear sale details
            saleDetailsArea.clear();
            showAlert("Success", "Sale completed successfully");
        } catch (Exception e) {
            showAlert("Error", "Failed to complete sale: " + e.getMessage());
        }
    }

    // Report generation methods
    @FXML
    private void handleGenerateReport() {
        LocalDate date = reportDatePicker.getValue();
        List<Sale> dailySales = inventory.getSalesByDate(date);

        if (dailySales.isEmpty()) {
            reportArea.setText("No sales recorded for " + date.format(DateTimeFormatter.ISO_DATE));
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("Daily Sales Report for ").append(date.format(DateTimeFormatter.ISO_DATE)).append("\n\n");
        double totalSales = 0;

        for (Sale sale : dailySales) {
            report.append(sale.toString()).append("\n\n");
            totalSales += sale.getTotalAmount();
        }

        report.append(String.format("Total Sales for the Day: MWK%.2f", totalSales));
        reportArea.setText(report.toString());
    }

    @FXML
    private void handleDeleteReport() {
        LocalDate date = reportDatePicker.getValue();
        if (date == null) {
            showAlert("Error", "Please select a date to delete the report");
            return;
        }

        List<Sale> dailySales = inventory.getSalesByDate(date);
        if (dailySales.isEmpty()) {
            showAlert("Error", "No sales recorded for the selected date");
            return;
        }

        // Remove sales for the selected date
        inventory.getAllSales().removeIf(sale -> sale.getDate().equals(date));
        saveInventory();
        reportArea.clear();
        showAlert("Success", "Sales report for " + date.format(DateTimeFormatter.ISO_DATE) + " has been deleted");
    }

    // Utility methods
    private void saveInventory() {
        try {
            FileUtil.saveInventory(inventory);
        } catch (IOException e) {
            showAlert("Error", "Failed to save inventory data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}