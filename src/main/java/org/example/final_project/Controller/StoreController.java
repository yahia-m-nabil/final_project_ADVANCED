package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.final_project.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class StoreController {

    // --- FXML Injections ---
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
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
     * Initializes the controller. Sets up listeners for all UI controls
     * so the grid updates automatically when a filter changes.
     */
    @FXML
    public void initialize() {
        // 1. Setup Sorting Listener
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadProducts());

        // 2. Setup Filter Listeners (Reload grid whenever a checkbox is clicked)
        CheckBox[] allFilters = {
                checkChairs, checkDesks, checkTables,
                checkWood, checkMetal, checkPlastic,
                checkBrown, checkWhite, checkBlack
        };
        for (CheckBox cb : allFilters) {
            cb.selectedProperty().addListener((obs, old, newVal) -> loadProducts());
        }

        // 3. Initial Load
        loadProducts();
    }

    /**
     * Fetches items from all warehouses, applies filters/sorting, and populates the TilePane.
     * Public so it can be called to refresh stock display.
     */
    public void loadProducts() {
        productGrid.getChildren().clear();

        // Get all items using ECommerceSystem facade
        ArrayList<FurnitureItem> allItems = ECommerceSystem.getInstance().getAllItems();

        // Apply Search and Checkbox Filters
        ArrayList<FurnitureItem> filteredList = allItems.stream()
                .filter(this::passesSearch)
                .filter(this::passesFilters)
                .collect(Collectors.toCollection(ArrayList::new));

        // Apply Sorting
        applySorting(filteredList);

        // Inject Product Cards into Grid
        for (FurnitureItem item : filteredList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/ProductCard.fxml"));
                Node card = loader.load();

                // Set data in the card controller
                ProductCardController cardController = loader.getController();
                cardController.setData(item);

                productGrid.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean passesSearch(FurnitureItem item) {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) return true;
        return item.toString().toLowerCase().contains(query);
    }

    private boolean passesFilters(FurnitureItem item) {
        // Type Filter
        if (item instanceof Chair && !checkChairs.isSelected()) return false;
        if (item instanceof Desk && !checkDesks.isSelected()) return false;
        if (item instanceof Table && !checkTables.isSelected()) return false;

        // Material Filter
        if (item.getMaterial() == Materials.WOOD && !checkWood.isSelected()) return false;
        if (item.getMaterial() == Materials.METAL && !checkMetal.isSelected()) return false;
        if (item.getMaterial() == Materials.PLASTIC && !checkPlastic.isSelected()) return false;

        // Color Filter
        if (item.getColor() == Colors.BROWN && !checkBrown.isSelected()) return false;
        if (item.getColor() == Colors.WHITE && !checkWhite.isSelected()) return false;
        if (item.getColor() == Colors.BLACK && !checkBlack.isSelected()) return false;

        return true;
    }

    private void applySorting(ArrayList<FurnitureItem> list) {
        String sortType = sortComboBox.getValue();
        if (sortType == null) return;

        switch (sortType) {
            case "Price: Low to High":
                list.sort(Comparator.comparingInt(FurnitureItem::getPrice));
                break;
            case "Price: High to Low":
                list.sort((a, b) -> Integer.compare(b.getPrice(), a.getPrice()));
                break;
            case "Product ID":
                list.sort(Comparator.comparingInt(FurnitureItem::getItemID));
                break;
        }
    }

    @FXML
    private void handleSearch() {
        loadProducts(); // Re-runs loadProducts which checks searchField
    }

    @FXML
    private void goToCart(ActionEvent event) {
        switchScene(event, "/org/example/final_project/checkout.fxml", "My Cart");
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        switchScene(event, "/org/example/final_project/customerPage.fxml", "My Profile");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        switchScene(event, "/org/example/final_project/Login.fxml", "Login");
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Preserve window dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            stage.setScene(new Scene(root));
            stage.setTitle("FurnitureApp - " + title);

            // Restore window dimensions
            if (maximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}