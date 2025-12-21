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

public class CustomerController {

    // --- Sidebar & Profile Injections ---
    @FXML private Label sidebarName;
    @FXML private Label sidebarEmail;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField searchField;

    // --- Wishlist Table Injections ---
    @FXML private TableView<FurnitureItem> wishlistTable;
    @FXML private TableColumn<FurnitureItem, String> colWishItem;
    @FXML private TableColumn<FurnitureItem, String> colWishMaterial;
    @FXML private TableColumn<FurnitureItem, Double> colWishPrice;
    @FXML private TableColumn<FurnitureItem, Integer> colWishQty;

    // --- Order History Table Injections ---
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> colOrderId;
    @FXML private TableColumn<Order, String> colDate;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TableColumn<Order, Double> colTotal;

    @FXML
    public void initialize() {
        System.out.println("Customer Profile Initialized");
        setupTableFactories();
        loadCustomerData();
    }

    private void setupTableFactories() {
        colWishItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        colWishMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colWishPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colWishQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void loadCustomerData() {
        ECommerceSystem system = ECommerceSystem.getInstance();
        User currentUser = system.getCurrentUser();

        if (currentUser != null) {
            sidebarName.setText(currentUser.getName());
            sidebarEmail.setText(currentUser.getEmail());
            idField.setText(String.valueOf(currentUser.getMemberId()));
            nameField.setText(currentUser.getName());
            emailField.setText(currentUser.getEmail());

            ObservableList<FurnitureItem> wishlist = FXCollections.observableArrayList(currentUser.getWishlist());
            wishlistTable.setItems(wishlist);

            ObservableList<Order> orderHistory = FXCollections.observableArrayList(currentUser.getOrderHistory());
            orderTable.setItems(orderHistory);

            System.out.println("Displaying account details and history...");
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField != null ? searchField.getText() : "";
        if (!searchTerm.isEmpty()) {
            System.out.println("Searching for: " + searchTerm);
        }
    }

    @FXML
    private void goToCart() {
        navigateTo("/org/example/final_project/View/checkout.fxml", "Cart/Checkout");
    }

    @FXML
    private void goToStore() {
        navigateTo("/org/example/final_project/View/customer-store-home.fxml", "Store");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        ECommerceSystem system = ECommerceSystem.getInstance();
        system.clearSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("User logged out safely.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateTo(String fxmlPath, String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("Switching to " + pageName + "...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
