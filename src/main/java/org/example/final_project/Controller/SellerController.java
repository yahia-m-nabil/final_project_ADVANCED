package main.java.org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for SellerDashboard.fxml
 */
public class SellerController {

    // --- FXML Injections: Sidebar & Identity ---
    @FXML private Label sellerNameLabel;

    // --- FXML Injections: Stock Management Table ---
    @FXML private TableView<?> inventoryTable; // Replace ? with your FurnitureItem class
    @FXML private TableColumn<?, Integer> colItemId;
    @FXML private TableColumn<?, String> colItemName;
    @FXML private TableColumn<?, String> colMaterial;
    @FXML private TableColumn<?, String> colColor;
    @FXML private TableColumn<?, Double> colPrice;
    @FXML private TableColumn<?, Integer> colStock;

    // --- FXML Injections: Sales History Table ---
    @FXML private TableView<?> salesTable; // Replace ? with your Order class
    @FXML private TableColumn<?, String> colSaleId;
    @FXML private TableColumn<?, String> colSaleDate;
    @FXML private TableColumn<?, String> colSaleStatus;
    @FXML private TableColumn<?, Double> colSaleRevenue;

    /**
     * Called automatically when the FXML is loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("Seller Central Initialized");

        setupTableColumns();
        loadSellerData();
    }

    /**
     * Configures how the data should be displayed in the table columns.
     */
    private void setupTableColumns() {
        // TODO: Use colItemId.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        // Repeat for all columns in both inventoryTable and salesTable
    }

    /**
     * Populates the tables using the logic from Seller.java
     */
    private void loadSellerData() {
        // TODO: Bind the inventoryTable to the sellableFurniture ArrayList
        // TODO: Bind the salesTable to the salesHistory ArrayList
        System.out.println("Loading inventory and sales reports...");
    }

    // --- Action Methods ---

    /**
     * Triggered by "+ Add New Product" button
     */
    @FXML
    private void handleAddNewProduct(ActionEvent event) {
        // TODO: Open a Dialog or new Window (AddProductPopup.fxml)
        // This should allow adding Chair, Table, or Desk objects
        System.out.println("Opening 'Add New Product' form...");
    }

    /**
     * Triggered by Logout button
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        // TODO: Clear session and return to LoginView.fxml
        System.out.println("Seller logging out...");
    }

    /**
     * Optional: Placeholder for the Search TextField logic
     */
    @FXML
    private void handleSearchInventory() {
        // TODO: Filter the inventoryTable based on search text
    }
}