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
import javafx.util.Callback;
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
    @FXML private TableColumn<FurnitureItem, Void> colActions;

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
        // Wishlist table columns
        colWishItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        colWishMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colWishPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colWishQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Add Remove button to Actions column
        colActions.setCellFactory(new Callback<TableColumn<FurnitureItem, Void>, TableCell<FurnitureItem, Void>>() {
            @Override
            public TableCell<FurnitureItem, Void> call(TableColumn<FurnitureItem, Void> param) {
                return new TableCell<FurnitureItem, Void>() {
                    private final Button removeBtn = new Button("Remove");

                    {
                        removeBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-cursor: hand;");
                        removeBtn.setOnAction(event -> {
                            FurnitureItem item = getTableView().getItems().get(getIndex());
                            handleRemoveFromWishlist(item);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeBtn);
                        }
                    }
                };
            }
        });

        // Order history table columns
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
            // TODO: Implement search functionality
        }
    }

    /**
     * Remove item from wishlist/cart
     */
    private void handleRemoveFromWishlist(FurnitureItem item) {
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
            // Remove entire item from wishlist
            currentUser.removeFromWishlist(item.getItemID(), item.getQuantity());

            // Refresh the table
            loadCustomerData();

            System.out.println("Removed " + item.getName() + " from cart.");
        } catch (Exception e) {
            showAlert("Error", "Failed to remove item: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToCart(ActionEvent event) {
        navigateTo("/org/example/final_project/checkout.fxml", "Cart/Checkout", event);
    }

    @FXML
    private void goToStore(ActionEvent event) {
        navigateTo("/org/example/final_project/StoreView.fxml", "Store", event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        ECommerceSystem system = ECommerceSystem.getInstance();
        system.clearSession();
        navigateTo("/org/example/final_project/Login.fxml", "Login", event);
        System.out.println("User logged out safely.");
    }

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

            System.out.println("Switching to " + pageName + "...");
        } catch (IOException e) {
            System.err.println("Error loading " + pageName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

