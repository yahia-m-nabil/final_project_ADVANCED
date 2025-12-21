package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.final_project.model.ECommerceSystem;

import java.io.IOException;

public class StoreController {

    // --- FXML Injections ---
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ComboBox<String> regionComboBox; // From your previous request
    @FXML private TilePane productGrid;

    // Category Checkboxes
    @FXML private CheckBox checkChairs;
    @FXML private CheckBox checkDesks;
    @FXML private CheckBox checkTables;

    // Material Checkboxes
    @FXML private CheckBox checkWood;
    @FXML private CheckBox checkMetal;
    @FXML private CheckBox checkPlastic;

    // Color Checkboxes
    @FXML private CheckBox checkBrown;
    @FXML private CheckBox checkWhite;
    @FXML private CheckBox checkBlack;

    /**
     * This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("Store GUI Initialized");

        // 1. Setup Sorting Listener
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleSorting(newVal);
            }
        });

        // 2. Setup Filter Listeners (example for one)
        checkChairs.selectedProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        checkDesks.selectedProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        checkTables.selectedProperty().addListener((obs, oldVal, newVal) -> updateFilters());

        // 3. Initial Load of Products
        loadProducts();
    }

    // --- Placeholder Methods for your Logic ---

    private void loadProducts() {
        // TODO: Get items from WarehouseList.getAllItems()
        // TODO: Loop through items and inject ProductCard.fxml into productGrid
        System.out.println("Loading all products into grid...");
    }

    private void handleSorting(String sortType) {
        // TODO: Integrate with warehouse.sortInventoryByPrice() or sortInventoryByID()
        System.out.println("Sorting products by: " + sortType);
    }

    private void updateFilters() {
        // TODO: Filter the list based on which checkboxes are selected
        System.out.println("Updating product grid based on filters...");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        System.out.println("Searching for: " + query);
        // TODO: Filter warehouse list by name/ID
    }

    @FXML
    private void goToCart(ActionEvent event) {
        navigateTo("/org/example/final_project/checkout.fxml", "Cart/Checkout", event);
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        navigateTo("/org/example/final_project/customerPage.fxml", "Customer Profile", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        ECommerceSystem system = ECommerceSystem.getInstance();
        system.clearSession();
        navigateTo("/org/example/final_project/Login.fxml", "Login", event);
        System.out.println("User logged out successfully.");
    }

    /**
     * Helper method to navigate to different scenes.
     */
    private void navigateTo(String fxmlPath, String pageName, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("Navigating to " + pageName + "...");
        } catch (IOException e) {
            System.err.println("Error loading " + pageName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
