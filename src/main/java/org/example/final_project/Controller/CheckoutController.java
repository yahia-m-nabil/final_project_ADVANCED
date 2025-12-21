package main.java.org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class CheckoutController {

    // --- Table Injections ---
    @FXML private TableView<?> checkoutTable; // Replace ? with FurnitureItem
    @FXML private TableColumn<?, String> colItem;
    @FXML private TableColumn<?, Integer> colQty;
    @FXML private TableColumn<?, Double> colPrice;
    @FXML private TableColumn<?, Double> colSubtotal;

    // --- Summary Labels ---
    @FXML private Label itemsTotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label finalTotalLabel;

    // --- Selection & Actions ---
    @FXML private ComboBox<String> warehouseSelector;
    @FXML private Button placeOrderButton;

    private double totalAmount = 0.0;

    /**
     * Initializes the checkout screen.
     */
    @FXML
    public void initialize() {
        System.out.println("Checkout Screen Initialized");

        setupColumns();
        populateWarehouseList();
        calculateTotals();
    }

    /**
     * Map table columns to FurnitureItem properties.
     */
    private void setupColumns() {
        // TODO: colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        // TODO: colSubtotal logic (Price * Quantity)
    }

    /**
     * Load warehouse names from your WarehouseList.
     */
    private void populateWarehouseList() {
        // TODO: warehouseSelector.setItems( WarehouseList.getWarehouseNames() );
    }

    /**
     * Math logic for order summary.
     */
    private void calculateTotals() {
        // TODO: Loop through items in user's wishlist/cart
        // double sub = 0;
        // for (FurnitureItem item : cart) sub += item.getPrice() * item.getQuantity();

        // double tax = sub * 0.15;
        // this.totalAmount = sub + tax;

        // itemsTotalLabel.setText(String.format("$%.2f", sub));
        // taxLabel.setText(String.format("$%.2f", tax));
        // finalTotalLabel.setText(String.format("$%.2f", totalAmount));
    }

    /**
     * Handles the 'Confirm & Pay' logic.
     */
    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        String selectedWH = warehouseSelector.getValue();

        if (selectedWH == null) {
            showAlert("Selection Required", "Please choose a pickup warehouse.");
            return;
        }

        System.out.println("Processing payment for: " + finalTotalLabel.getText());

        /* TODO: Logic flow:
           1. Check if User.getMoney() >= totalAmount
           2. Deduct money: user.setMoney(user.getMoney() - totalAmount)
           3. Deduct stock: warehouse.removeFromInventory(...)
           4. Create new Order and add to user.getOrderHistory()
           5. Clear user.getWishlist()
        */

        showSuccess("Order Placed", "Your furniture is ready for pickup at " + selectedWH);
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