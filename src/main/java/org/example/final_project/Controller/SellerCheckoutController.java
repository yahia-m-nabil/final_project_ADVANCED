package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.final_project.model.*;

import java.io.IOException;
import java.util.ArrayList;

public class SellerCheckoutController {

    @FXML private TableView<FurnitureItem> inventoryTable;
    @FXML private TableColumn<FurnitureItem, String> colItem;
    @FXML private TableColumn<FurnitureItem, Integer> colQty;
    @FXML private TableColumn<FurnitureItem, Double> colPrice;
    @FXML private TableColumn<FurnitureItem, Double> colSubtotal;
    @FXML private TableColumn<FurnitureItem, Void> colActions;

    @FXML private Label grossEarningsLabel;
    @FXML private Label platformFeeLabel;
    @FXML private Label netEarningsLabel;

    @FXML private ComboBox<String> warehouseSelector;
    @FXML private Button supplyButton;

    private ECommerceSystem system;
    private Seller currentSeller;
    private double grossAmount = 0.0;
    private double netAmount = 0.0;

    @FXML
    public void initialize() {
        System.out.println("Seller Checkout Initialized");

        system = ECommerceSystem.getInstance();
        currentSeller = system.getCurrentSeller();

        if (currentSeller == null) {
            showAlert("Session Error", "No seller logged in.");
            return;
        }

        setupColumns();
        populateWarehouseList();
        loadInventory();
        calculateEarnings();
    }

    private void setupColumns() {
        colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colSubtotal.setCellValueFactory(cellData -> {
            FurnitureItem item = cellData.getValue();
            double subtotal = item.getPrice() * item.getQuantity();
            return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
        });

        colActions.setCellFactory(column -> new TableCell<FurnitureItem, Void>() {
            private final Button removeBtn = new Button("Remove");
            {
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px; -fx-padding: 5 10;");
                removeBtn.setOnAction(event -> {
                    FurnitureItem item = getTableView().getItems().get(getIndex());
                    handleRemoveFromInventory(item);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });
    }

    private void populateWarehouseList() {
        ArrayList<Warehouse> warehouses = system.getAllWarehouses();
        ObservableList<String> warehouseNames = FXCollections.observableArrayList();

        for (Warehouse warehouse : warehouses) {
            warehouseNames.add(warehouse.getLocation());
        }

        warehouseSelector.setItems(warehouseNames);

        if (!warehouseNames.isEmpty()) {
            warehouseSelector.getSelectionModel().selectFirst();
        }
    }

    private void loadInventory() {
        ArrayList<FurnitureItem> inventory = currentSeller.getSellableFurniture();
        ObservableList<FurnitureItem> items = FXCollections.observableArrayList(inventory);
        inventoryTable.setItems(items);
        System.out.println("Loaded " + inventory.size() + " items");
    }

    private void calculateEarnings() {
        int gross = currentSeller.calculateMoney();
        int platformFee = (gross * 5) / 100;
        int net = gross - platformFee;

        this.grossAmount = gross;
        this.netAmount = net;

        grossEarningsLabel.setText(String.format("$%d", gross));
        platformFeeLabel.setText(String.format("$%d", platformFee));
        netEarningsLabel.setText(String.format("$%d", net));
    }

    private void handleRemoveFromInventory(FurnitureItem item) {
        if (item == null) {
            return;
        }

        try {
            currentSeller.removeFromInventory(item.getItemID(), item.getQuantity());
            loadInventory();
            calculateEarnings();
            System.out.println("Removed " + item.getName() + " from inventory.");
        } catch (Exception e) {
            showAlert("Error", "Failed to remove item: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupplyToWarehouse(ActionEvent event) {
        if (currentSeller == null) {
            showAlert("Login Required", "Please log in as a seller.");
            return;
        }

        String selectedWH = warehouseSelector.getValue();
        if (selectedWH == null) {
            showAlert("Selection Required", "Please choose a warehouse.");
            return;
        }

        ArrayList<FurnitureItem> inventory = currentSeller.getSellableFurniture();
        if (inventory.isEmpty()) {
            showAlert("Empty Inventory", "Your inventory is empty. Add items before supplying.");
            return;
        }

        Warehouse warehouse = system.findWarehouseByLocation(selectedWH);
        if (warehouse == null) {
            showAlert("Error", "Selected warehouse not found.");
            return;
        }

        try {
            // Supply all items to warehouse
            currentSeller.supplyAllToWarehouse(warehouse);

            // Show success message
            showSuccess("Supply Successful",
                String.format("Your inventory has been supplied to %s warehouse.\nYou received: $%d\nNew Balance: $%d",
                selectedWH, (int)netAmount, currentSeller.getMoney()));

            // Navigate back to seller page
            goBackToSellerPage(event);

        } catch (IllegalArgumentException e) {
            showAlert("Supply Error", e.getMessage());
        } catch (Exception e) {
            showAlert("Supply Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToSellerPage(ActionEvent event) {
        navigateTo("/org/example/final_project/sellerPage.fxml", "Seller Dashboard", event);
    }

    private void navigateTo(String fxmlPath, String pageName, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            stage.setScene(new Scene(root));

            if (maximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
            }

            System.out.println("Navigating to " + pageName + "...");
        } catch (IOException e) {
            System.err.println("Error loading " + pageName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Payment Received!");
        alert.setContentText(content);
        alert.showAndWait();
    }
}

