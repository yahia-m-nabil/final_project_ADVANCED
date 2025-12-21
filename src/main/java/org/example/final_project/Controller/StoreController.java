package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

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
        // TODO: Switch scene to CheckoutView.fxml
        System.out.println("Navigating to Cart...");
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        // TODO: Switch scene to CustomerProfileView.fxml
        System.out.println("Navigating to Profile...");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // TODO: Clear session and return to LoginView.fxml
        System.out.println("Logging out...");
    }
}
