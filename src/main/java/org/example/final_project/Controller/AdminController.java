package org.example.final_project.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.final_project.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class AdminController {

    @FXML private Label welcomeLabel;
    @FXML private Label userCountLabel;
    @FXML private Label sellerCountLabel;
    @FXML private Label productCountLabel;
    @FXML private StackPane contentPane;

    private Admin currentAdmin;

    @FXML
    public void initialize() {
        ECommerceSystem system = ECommerceSystem.getInstance();

        // Get current admin
        if (system.getCurrentMember() instanceof Admin) {
            currentAdmin = (Admin) system.getCurrentMember();
            welcomeLabel.setText("Welcome, " + currentAdmin.getName());
        }

        // Update Statistics
        updateStats();
    }

    private void updateStats() {
        ECommerceSystem system = ECommerceSystem.getInstance();
        ArrayList<Member> allMembers = system.getMembersData().getAllMembers();

        // Count Customers (only regular Users) - using simple loops (KISS)
        int customerCount = 0;
        for (Member m : allMembers) {
            if (m.getClass() == User.class) {
                customerCount++;
            }
        }

        // Count Sellers
        int sellerCount = 0;
        for (Member m : allMembers) {
            if (m instanceof Seller) {
                sellerCount++;
            }
        }

        // Count Total Products
        int totalProducts = 0;
        for (Warehouse wh : system.getAllWarehouses()) {
            totalProducts += wh.getInventory().size();
        }

        userCountLabel.setText(String.valueOf(customerCount));
        sellerCountLabel.setText(String.valueOf(sellerCount));
        productCountLabel.setText(String.valueOf(totalProducts));
    }

    @FXML
    private void showDashboard() {
        contentPane.getChildren().clear();
        Label label = new Label("Select an option from the sidebar.");
        label.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 18;");
        contentPane.getChildren().add(label);
        updateStats();
    }

    @FXML
    private void manageUsers() {
        contentPane.getChildren().clear();

        VBox container = new VBox(15);
        container.setPadding(new Insets(10));

        // Title
        Label title = new Label("Customer Management");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Add User Button
        Button addUserBtn = new Button("+ Add New Customer");
        addUserBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        addUserBtn.setOnAction(event -> showAddUserDialog());

        // Table
        TableView<User> table = new TableView<>();

        TableColumn<User, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMemberId())));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<User, String> ordersCol = new TableColumn<>("Orders");
        ordersCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getOrderHistory().size())));

        TableColumn<User, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        //noinspection unchecked
        table.getColumns().addAll(idCol, nameCol, emailCol, ordersCol, actionsCol);

        // Load data
        ObservableList<User> users = FXCollections.observableArrayList(currentAdmin.getAllUsers());
        table.setItems(users);

        container.getChildren().addAll(title, addUserBtn, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentPane.getChildren().add(container);
    }

    @FXML
    private void manageSellers() {
        contentPane.getChildren().clear();

        VBox container = new VBox(15);
        container.setPadding(new Insets(10));

        Label title = new Label("Seller Management");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button addSellerBtn = new Button("+ Add New Seller");
        addSellerBtn.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        addSellerBtn.setOnAction(event -> showAddSellerDialog());

        TableView<Seller> table = new TableView<>();

        TableColumn<Seller, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMemberId())));

        TableColumn<Seller, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Seller, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Seller, String> salesCol = new TableColumn<>("Sales");
        salesCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSalesHistory().size())));

        TableColumn<Seller, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(event -> {
                    Seller seller = getTableView().getItems().get(getIndex());
                    deleteSeller(seller);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        //noinspection unchecked
        table.getColumns().addAll(idCol, nameCol, emailCol, salesCol, actionsCol);

        ObservableList<Seller> sellers = FXCollections.observableArrayList(currentAdmin.getAllSellers());
        table.setItems(sellers);

        container.getChildren().addAll(title, addSellerBtn, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentPane.getChildren().add(container);
    }

    @FXML
    private void manageProducts() {
        contentPane.getChildren().clear();

        VBox container = new VBox(15);
        container.setPadding(new Insets(10));

        Label title = new Label("Product Management");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox actionBar = new HBox(10);
        Button applyDiscountBtn = new Button("Apply Global Discount");
        applyDiscountBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        applyDiscountBtn.setOnAction(event -> showApplyDiscountDialog());
        actionBar.getChildren().add(applyDiscountBtn);

        TableView<FurnitureItem> table = new TableView<>();

        TableColumn<FurnitureItem, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getItemID())));

        TableColumn<FurnitureItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<FurnitureItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));

        TableColumn<FurnitureItem, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%d", data.getValue().getPrice())));

        TableColumn<FurnitureItem, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));

        TableColumn<FurnitureItem, String> discountCol = new TableColumn<>("Discountable");
        discountCol.setCellValueFactory(data -> {
            if (data.getValue() instanceof Discountable) {
                return new SimpleStringProperty("Yes");
            }
            return new SimpleStringProperty("No");
        });

        //noinspection unchecked
        table.getColumns().addAll(idCol, nameCol, typeCol, priceCol, stockCol, discountCol);

        ObservableList<FurnitureItem> products = FXCollections.observableArrayList(currentAdmin.getAllItemsAcrossWarehouses());
        table.setItems(products);

        container.getChildren().addAll(title, actionBar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentPane.getChildren().add(container);
    }

    @FXML
    private void manageWarehouses() {
        contentPane.getChildren().clear();

        VBox container = new VBox(15);
        container.setPadding(new Insets(10));

        Label title = new Label("Warehouse Management");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button addWarehouseBtn = new Button("+ Add New Warehouse");
        addWarehouseBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        addWarehouseBtn.setOnAction(event -> showAddWarehouseDialog());

        TableView<Warehouse> table = new TableView<>();

        TableColumn<Warehouse, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));

        TableColumn<Warehouse, String> itemsCol = new TableColumn<>("Total Items");
        itemsCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getInventory().size())));

        TableColumn<Warehouse, String> capacityCol = new TableColumn<>("Total Stock");
        capacityCol.setCellValueFactory(data -> {
            int totalStock = 0;
            for (FurnitureItem item : data.getValue().getInventory()) {
                totalStock += item.getQuantity();
            }
            return new SimpleStringProperty(String.valueOf(totalStock));
        });

        TableColumn<Warehouse, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(event -> {
                    Warehouse warehouse = getTableView().getItems().get(getIndex());
                    deleteWarehouse(warehouse);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        //noinspection unchecked
        table.getColumns().addAll(locationCol, itemsCol, capacityCol, actionsCol);

        ObservableList<Warehouse> warehouses = FXCollections.observableArrayList(currentAdmin.getAllWarehouses());
        table.setItems(warehouses);

        container.getChildren().addAll(title, addWarehouseBtn, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        contentPane.getChildren().add(container);
    }

    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter customer details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String email = emailField.getText();
                    return currentAdmin.addUser(id, name, email);
                } catch (Exception e) {
                    showAlert("Error", "Failed to add customer: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        if (result.isPresent()) {
            manageUsers();
            updateStats();
            showAlert("Success", "Customer added successfully!");
        }
    }

    private void showAddSellerDialog() {
        Dialog<Seller> dialog = new Dialog<>();
        dialog.setTitle("Add New Seller");
        dialog.setHeaderText("Enter seller details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String email = emailField.getText();
                    return currentAdmin.addSeller(id, name, email);
                } catch (Exception e) {
                    showAlert("Error", "Failed to add seller: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Seller> result = dialog.showAndWait();
        if (result.isPresent()) {
            manageSellers();
            updateStats();
            showAlert("Success", "Seller added successfully!");
        }
    }

    private void showAddWarehouseDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Warehouse");
        dialog.setHeaderText("Enter warehouse location");
        dialog.setContentText("Location:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(location -> {
            try {
                currentAdmin.addWarehouse(location);
                manageWarehouses();
                updateStats();
                showAlert("Success", "Warehouse added successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to add warehouse: " + e.getMessage());
            }
        });
    }

    private void showApplyDiscountDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Apply Global Discount");
        dialog.setHeaderText("Enter discount percentage");
        dialog.setContentText("Discount %:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(discountStr -> {
            try {
                double discount = Double.parseDouble(discountStr);
                int itemsDiscounted = currentAdmin.applyGlobalDiscount(discount);
                manageProducts();
                showAlert("Success", "Discount applied to " + itemsDiscounted + " items!");
            } catch (Exception e) {
                showAlert("Error", "Failed to apply discount: " + e.getMessage());
            }
        });
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText("Are you sure you want to delete this customer?");
        alert.setContentText("Customer: " + user.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                currentAdmin.removeMember(user.getMemberId());
                manageUsers();
                updateStats();
                showAlert("Success", "Customer deleted successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete customer: " + e.getMessage());
            }
        }
    }

    private void deleteSeller(Seller seller) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Seller");
        alert.setHeaderText("Are you sure you want to delete this seller?");
        alert.setContentText("Seller: " + seller.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                currentAdmin.removeMember(seller.getMemberId());
                manageSellers();
                updateStats();
                showAlert("Success", "Seller deleted successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete seller: " + e.getMessage());
            }
        }
    }

    private void deleteWarehouse(Warehouse warehouse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Warehouse");
        alert.setHeaderText("Are you sure you want to delete this warehouse?");
        alert.setContentText("Location: " + warehouse.getLocation());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                currentAdmin.removeWarehouse(warehouse.getLocation());
                manageWarehouses();
                updateStats();
                showAlert("Success", "Warehouse deleted successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete warehouse: " + e.getMessage());
            }
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
    private void handleLogout(ActionEvent event) {
        try {
            ECommerceSystem.getInstance().clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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

            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load login page: " + e.getMessage());
        }
    }
}

