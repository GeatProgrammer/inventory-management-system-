
 INVENTORY MANAGEMENT SYSTEM


DESCRIPTION
A simple Java-based Inventory Management System tailored for a small shop. It allows the user to manage products, process sales, and generate daily sales reports via a user-friendly GUI built with JavaFX.

KEY FEATURES
-Add, Delete, Modify, and View Products
-Process Sales Transactions
-Generate Daily Sales Reports
-JavaFX-based GUI Interface
-File Handling for data persistence (products and sales stored in `.dat` files)
-Exception Handling to ensure robust user interaction and error reporting

 HOW TO RUN THE PROGRAM

Prerequisites
- Java Development Kit (JDK) 11 or higher
- JavaFX SDK (if not bundled with your JDK)
- An IDE such as IntelliJ IDEA, Eclipse, or VS Code with JavaFX setup

Running Instructions
1. Clone or Download the Repository
2. Set up JavaFX in your IDE
   - Include JavaFX SDK in your project’s library/module path.
   - VM options example (if running from terminal or configuration):  
     
     --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
     
3. Compile and Run the MainApp.java file

 Files Overview
- MainApp.java: Entry point of the application
- Product.java: Defines the product structure
- Sale.java: Defines a sale transaction
- main.fxml and MainController.java: JavaFX GUI logic


HOW TO USE THE APPLICATION
Product Management Tab:
-Add products by filling in the ID, name, price, and quantity fields

-Delete products by selecting them in the table and clicking "Delete  Selected"

Sales Processing Tab:

Select a product from the dropdown
-Enter quantity and click "Add to Sale"

-Review the sale details and click "Complete Sale" to finalize

Sales Reports Tab:

-Select a date and click "Generate Report"

-View the daily sales summary in the text area

-select delete in order to delete a report

COLLABORATOR 'S  ADDRESS: stanslausphillimon@gmail.com
