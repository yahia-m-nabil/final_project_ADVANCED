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

public class CheckoutController {

    // --- Table Injections ---
    @FXML private TableView<FurnitureItem> checkoutTable;
    @FXML private TableColumn<FurnitureItem, String> colItem;
    @FXML private TableColumn<FurnitureItem, Integer> colQty;
    @FXML private TableColumn<FurnitureItem, Double> colPrice;
    @FXML private TableColumn<FurnitureItem, Double> colSubtotal;
    @FXML private TableColumn<FurnitureItem, Void> colActions;

    // --- Summary Labels ---
    @FXML private Label itemsTotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label finalTotalLabel;
    @FXML private Label warehouseLabel;

    // --- Actions ---
    @FXML private Button placeOrderButton;

    private double totalAmount = 0.0;

    /**
     * Initializes the checkout screen.
     */
    @FXML
    public void initialize() {
        System.out.println("Checkout Screen Initialized");

        setupColumns();
        displaySelectedWarehouse();
        loadCartItems();
        calculateTotals();
    }

    private void displaySelectedWarehouse() {
        ECommerceSystem system = ECommerceSystem.getInstance();
        User currentUser = system.getCurrentUser();

        if (currentUser != null && currentUser.getSelectedWarehouse() != null) {
            String location = currentUser.getSelectedWarehouse().getLocation();
            warehouseLabel.setText(location + " Warehouse");
        } else {
            warehouseLabel.setText("No warehouse selected");
            warehouseLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }

    /**
     * Map table columns to FurnitureItem properties.
     */
    private void setupColumns() {
        colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Custom cell value factory for subtotal (price * quantity)
        colSubtotal.setCellValueFactory(cellData -> {
            FurnitureItem item = cellData.getValue();
            double subtotal = item.getPrice() * item.getQuantity();
            return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
        });

        // Actions column - Remove button
        colActions.setCellFactory(column -> new TableCell<FurnitureItem, Void>() {
            private final Button removeBtn = new Button("Remove");

            {
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px; -fx-padding: 5 10;");
                removeBtn.setOnAction(event -> {
                    FurnitureItem item = getTableView().getItems().get(getIndex());
                    handleRemoveFromCart(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });
    }

    private void loadCartItems() {
        ECommerceSystem system = ECommerceSystem.getInstance();
        User currentUser = system.getCurrentUser();

        if (currentUser != null) {
            ArrayList<FurnitureItem> wishlist = currentUser.getWishlist();
            ObservableList<FurnitureItem> cartItems = FXCollections.observableArrayList(wishlist);
            checkoutTable.setItems(cartItems);
            System.out.println("Loaded " + wishlist.size() + " items into cart");
        } else {
            System.out.println("No user logged in - cart is empty");
        }
    }


    private void calculateTotals() {
        ECommerceSystem system = ECommerceSystem.getInstance();
        User currentUser = system.getCurrentUser();

        if (currentUser != null) {
            // Use User model methods for calculations (DRY principle)
            double subtotal = currentUser.getWishlistSubtotal();
            double tax = currentUser.getWishlistTax();
            this.totalAmount = currentUser.calculateMoney();

            itemsTotalLabel.setText(String.format("$%.2f", subtotal));
            taxLabel.setText(String.format("$%.2f", tax));
            finalTotalLabel.setText(String.format("$%.2f", totalAmount));
        } else {
            itemsTotalLabel.setText("$0.00");
            taxLabel.setText("$0.00");
            finalTotalLabel.setText("$0.00");
        }
    }


    /**
     * Removes an item from the cart and refreshes the view.
     */
    private void handleRemoveFromCart(FurnitureItem item) {
        if (item == null) {
            return;
        }

        ECommerceSystem system = ECommerceSystem.getInstance();
        User currentUser = system.getCurrentUser();

        if (currentUser == null) {
            showAlert("Error", "User not logged in.");
            return;
        }

        try {
            // Remove the entire item from wishlist
            currentUser.removeFromWishlist(item.getItemID(), item.getQuantity());

            // Reload the cart and recalculate totals
            loadCartItems();
            calculateTotals();

            System.out.println("Removed " + item.getName() + " from cart.");
        } catch (Exception e) {
            showAlert("Error", "Failed to remove item: " + e.getMessage());
        }
    }

    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        ECommerceSystem system = ECommerceSystem.getInstance();
        User currentUser = system.getCurrentUser();

        if (currentUser == null) {
            showAlert("Login Required", "Please log in to place an order.");
            return;
        }

        // Get pre-selected warehouse from user
        Warehouse warehouse = currentUser.getSelectedWarehouse();
        if (warehouse == null) {
            showAlert("Selection Required", "Please select a warehouse from the store page first.");
            return;
        }

        ArrayList<FurnitureItem> wishlist = currentUser.getWishlist();
        if (wishlist.isEmpty()) {
            showAlert("Empty Cart", "Your cart is empty. Add items before checkout.");
            return;
        }

        // Check if user has enough money
        if (currentUser.getMoney() < totalAmount) {
            showAlert("Insufficient Funds",
                String.format("You need $%.2f but only have $%d. Please add funds to your account.",
                totalAmount, currentUser.getMoney()));
            return;
        }

        // Check stock availability
        String warehouseLocation = warehouse.getLocation();
        for (FurnitureItem item : wishlist) {
            if (!warehouse.hasEnoughStock(item.getItemID(), item.getQuantity())) {
                showAlert("Insufficient Stock",
                    String.format("Not enough stock for %s at %s warehouse.", item.getName(), warehouseLocation));
                return;
            }
        }

        try {
            // Perform checkout - this handles everything: deduct money, create order, clear wishlist, remove stock
            currentUser.checkout(warehouse);

            // Get the newly created order
            ArrayList<Order> orderHistory = currentUser.getOrderHistory();
            Order newOrder = orderHistory.get(orderHistory.size() - 1);

            // Show success message
            showSuccess("Order Placed Successfully",
                String.format("Your order #%d is ready for pickup at %s.\nTotal paid: $%.2f\nRemaining balance: $%d",
                newOrder.getOrderId(), warehouseLocation, totalAmount, currentUser.getMoney()));

            // Navigate back to store after successful order
            goBackToStore(event);

        } catch (IllegalArgumentException e) {
            showAlert("Order Error", e.getMessage());
        } catch (Exception e) {
            showAlert("Order Error", "An error occurred while processing your order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigate back to the store view.
     */
    @FXML
    private void goBackToStore(ActionEvent event) {
        navigateTo("/org/example/final_project/StoreView.fxml", "Store", event);
    }

    /**
     * Helper method to navigate to different scenes.
     */
    private void navigateTo(String fxmlPath, String pageName, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Preserve window dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            stage.setScene(new Scene(root));

            // Restore window dimensions
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
        alert.setHeaderText("Payment Successful!");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
