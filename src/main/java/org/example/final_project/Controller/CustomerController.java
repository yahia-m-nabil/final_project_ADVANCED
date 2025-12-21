package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

public class CustomerController {

    // --- Sidebar & Profile Injections ---
    @FXML private Label sidebarName;
    @FXML private Label sidebarEmail;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;

    // --- Wishlist Table Injections ---
    @FXML private TableView<?> wishlistTable; // Replace ? with FurnitureItem
    @FXML private TableColumn<?, String> colWishItem;
    @FXML private TableColumn<?, String> colWishMaterial;
    @FXML private TableColumn<?, Double> colWishPrice;
    @FXML private TableColumn<?, Integer> colWishQty;

    // --- Order History Table Injections ---
    @FXML private TableView<?> orderTable; // Replace ? with Order
    @FXML private TableColumn<?, String> colOrderId;
    @FXML private TableColumn<?, String> colDate;
    @FXML private TableColumn<?, String> colStatus;
    @FXML private TableColumn<?, Double> colTotal;

    /**
     * Called when the profile page is loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("Customer Profile Initialized");

        setupTableFactories();
        loadCustomerData();
    }

    /**
     * Connects table columns to your Java object properties.
     */
    private void setupTableFactories() {
        // TODO: colWishItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        // TODO: colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        // ... repeat for all columns
    }

    /**
     * Populates fields and tables from the current logged-in User object.
     */
    private void loadCustomerData() {
        // TODO: User currentUser = Session.getCurrentUser();
        // sidebarName.setText(currentUser.getName());
        // idField.setText(String.valueOf(currentUser.getMemberId()));

        // TODO: wishlistTable.setItems(currentUser.getWishlist());
        // TODO: orderTable.setItems(currentUser.getOrderHistory());

        System.out.println("Displaying account details and history...");
    }

    // --- Navigation Actions ---

    @FXML
    private void handleSearch() {
        // TODO: Logic for top-bar search
    }

    @FXML
    private void goToCart() {
        // TODO: Navigate to CheckoutView.fxml
        System.out.println("Switching to Cart/Checkout...");
    }

    @FXML
    private void goToStore() {
        // TODO: Navigate to CustomerStoreHome.fxml
        System.out.println("Returning to Store...");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // TODO: Clear session and return to LoginView.fxml
        System.out.println("User logged out safely.");
    }
}
