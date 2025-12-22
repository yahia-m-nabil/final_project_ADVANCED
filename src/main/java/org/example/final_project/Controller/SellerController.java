package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.example.final_project.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller for SellerDashboard.fxml
 */
public class SellerController {

    // --- FXML Injections: Sidebar & Identity ---
    @FXML private Label sellerNameLabel;

    // --- FXML Injections: Stock Management Table ---
    @FXML private TableView<FurnitureItem> inventoryTable;
    @FXML private TableColumn<FurnitureItem, Integer> colItemId;
    @FXML private TableColumn<FurnitureItem, String> colItemName;
    @FXML private TableColumn<FurnitureItem, String> colMaterial;
    @FXML private TableColumn<FurnitureItem, String> colColor;
    @FXML private TableColumn<FurnitureItem, Double> colPrice;
    @FXML private TableColumn<FurnitureItem, Integer> colStock;

    // --- FXML Injections: Sales History Table ---
    @FXML private TableView<Order> salesTable;
    @FXML private TableColumn<Order, Integer> colSaleId;
    @FXML private TableColumn<Order, String> colSaleDate;
    @FXML private TableColumn<Order, String> colSaleStatus;
    @FXML private TableColumn<Order, Double> colSaleRevenue;

    // --- FXML Injections: Search Field ---
    @FXML private TextField searchField;

    private ECommerceSystem system;
    private Seller currentSeller;
    private ObservableList<FurnitureItem> inventoryList;
    private ObservableList<Order> salesList;

    /**
     * Called automatically when the FXML is loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("Seller Central Initialized");

        system = ECommerceSystem.getInstance();
        currentSeller = system.getCurrentSeller();

        if (currentSeller == null) {
            showError("Session Error", "No seller logged in.");
            return;
        }

        sellerNameLabel.setText(currentSeller.getName());

        setupTableColumns();
        loadSellerData();
    }

    /**
     * Configures how the data should be displayed in the table columns.
     */
    private void setupTableColumns() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colSaleId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSaleStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSaleRevenue.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    /**
     * Populates the tables using the logic from Seller.java
     */
    private void loadSellerData() {
        ArrayList<FurnitureItem> inventory = currentSeller.getSellableFurniture();
        inventoryList = FXCollections.observableArrayList(inventory);
        inventoryTable.setItems(inventoryList);

        ArrayList<Order> sales = currentSeller.getSalesHistory();
        salesList = FXCollections.observableArrayList(sales);
        salesTable.setItems(salesList);

        System.out.println("Loading inventory and sales reports...");
    }

    // --- Action Methods ---

    /**
     * Triggered by "+ Add New Product" button
     */
    @FXML
    private void handleAddNewProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/AddProductDialog.fxml"));
            GridPane dialogContent = loader.load();
            AddProductDialogController dialogController = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add New Product");
            dialog.setHeaderText("Add a new furniture item to your inventory");

            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(dialogContent);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == addButtonType) {
                if (!dialogController.isValid()) {
                    showError("Invalid Input", "Please select all options.");
                    return;
                }

                int itemId = dialogController.generateItemId();
                String type = dialogController.getType();
                Materials material = dialogController.getMaterial();
                Colors color = dialogController.getColor();
                int quantity = dialogController.getQuantity();

                // Check if item already exists before adding
                FurnitureItem existing = currentSeller.findItemInInventory(itemId);
                boolean itemExists = (existing != null);
                int previousQuantity = itemExists ? existing.getQuantity() : 0;

                // Add the item (will either create new or add to existing)
                currentSeller.createFurnitureItem(itemId, quantity, material, color, type);
                loadSellerData();

                // Show appropriate message
                String itemName = color + " " + material + " " + type;
                if (itemExists) {
                    showInfo("Quantity Updated",
                        String.format("Added %d more %s to inventory!\nPrevious: %d, New Total: %d",
                        quantity, itemName, previousQuantity, previousQuantity + quantity));
                } else {
                    showInfo("Success",
                        String.format("Added %d x %s to inventory!", quantity, itemName));
                }
            }

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Quantity must be a number.");
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    /**
     * Triggered by Logout button
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        system.clearSession();
        switchScene(event, "/org/example/final_project/Login.fxml", "Login");
    }

    /**
     * Triggered by Supply to Warehouse button
     */
    @FXML
    private void handleSupplyToWarehouse(ActionEvent event) {
        if (currentSeller.getSellableFurniture().isEmpty()) {
            showError("Empty Inventory", "You have no items to supply. Add products first.");
            return;
        }
        switchScene(event, "/org/example/final_project/SellerCheckout.fxml", "Supply to Warehouse");
    }

    /**
     * Placeholder for the Search TextField logic
     */
    @FXML
    private void handleSearchInventory() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadSellerData();
            return;
        }

        ArrayList<FurnitureItem> allItems = currentSeller.getSellableFurniture();
        ArrayList<FurnitureItem> filteredItems = new ArrayList<>();

        for (FurnitureItem item : allItems) {
            String itemType = item.getType().toLowerCase();
            String material = item.getMaterial().toString().toLowerCase();
            String color = item.getColor().toString().toLowerCase();
            String itemId = String.valueOf(item.getItemID());

            if (itemType.contains(searchText) || material.contains(searchText) ||
                color.contains(searchText) || itemId.contains(searchText)) {
                filteredItems.add(item);
            }
        }

        inventoryList = FXCollections.observableArrayList(filteredItems);
        inventoryTable.setItems(inventoryList);
    }

    // --- Helper Methods ---

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            stage.setScene(new Scene(root));
            stage.setTitle(title);

            if (maximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
            }

            stage.show();

        } catch (IOException e) {
            showError("Navigation Error", "Could not load page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
