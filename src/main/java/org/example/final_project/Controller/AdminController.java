package org.example.final_project.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.final_project.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {

    // Style constants for dynamic elements
    private static final String DELETE_BTN_STYLE = "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 15;";

    @FXML private Label welcomeLabel;
    @FXML private Label userCountLabel;
    @FXML private Label sellerCountLabel;
    @FXML private Label productCountLabel;
    @FXML private StackPane contentPane;

    private Admin currentAdmin;

    @FXML
    public void initialize() {
        ECommerceSystem system = ECommerceSystem.getInstance();
        if (system.getCurrentMember() instanceof Admin) {
            currentAdmin = (Admin) system.getCurrentMember();
            welcomeLabel.setText("Welcome, " + currentAdmin.getName());
        }
        updateStats();
    }

    private void updateStats() {
        userCountLabel.setText(String.valueOf(currentAdmin.getTotalUsersCount()));
        sellerCountLabel.setText(String.valueOf(currentAdmin.getTotalSellersCount()));
        productCountLabel.setText(String.valueOf(currentAdmin.getTotalItemsCount()));
    }

    @FXML
    private void showDashboard() {
        contentPane.getChildren().clear();
        updateStats();
    }

    @FXML
    private void manageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/AdminUsersTable.fxml"));
            Parent view = loader.load();

            TableView<User> tableView = (TableView<User>) view.lookup("#tableView");
            Button addBtn = (Button) view.lookup("#addBtn");
            TableColumn<User, String> idCol = (TableColumn<User, String>) tableView.getColumns().get(0);
            TableColumn<User, String> nameCol = (TableColumn<User, String>) tableView.getColumns().get(1);
            TableColumn<User, String> emailCol = (TableColumn<User, String>) tableView.getColumns().get(2);
            TableColumn<User, String> ordersCol = (TableColumn<User, String>) tableView.getColumns().get(3);
            TableColumn<User, Void> actionsCol = (TableColumn<User, Void>) tableView.getColumns().get(4);

            idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMemberId())));
            nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
            ordersCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getOrderHistory().size())));

            actionsCol.setCellFactory(column -> new TableCell<User, Void>() {
                private final Button deleteBtn = new Button("Delete");
                {
                    deleteBtn.setStyle(DELETE_BTN_STYLE);
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

            List<User> users = currentAdmin.getAllUsers();
            for (User user : users) {
                tableView.getItems().add(user);
            }

            addBtn.setOnAction(e -> showAddUserDialog());

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            showAlert("Error", "Failed to load view: " + e.getMessage());
        }
    }

    @FXML
    private void manageSellers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/AdminSellersTable.fxml"));
            Parent view = loader.load();

            TableView<Seller> tableView = (TableView<Seller>) view.lookup("#tableView");
            Button addBtn = (Button) view.lookup("#addBtn");
            TableColumn<Seller, String> idCol = (TableColumn<Seller, String>) tableView.getColumns().get(0);
            TableColumn<Seller, String> nameCol = (TableColumn<Seller, String>) tableView.getColumns().get(1);
            TableColumn<Seller, String> emailCol = (TableColumn<Seller, String>) tableView.getColumns().get(2);
            TableColumn<Seller, String> salesCol = (TableColumn<Seller, String>) tableView.getColumns().get(3);
            TableColumn<Seller, Void> actionsCol = (TableColumn<Seller, Void>) tableView.getColumns().get(4);

            idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMemberId())));
            nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
            salesCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSalesHistory().size())));

            actionsCol.setCellFactory(column -> new TableCell<Seller, Void>() {
                private final Button deleteBtn = new Button("Delete");
                {
                    deleteBtn.setStyle(DELETE_BTN_STYLE);
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

            List<Seller> sellers = currentAdmin.getAllSellers();
            for (Seller seller : sellers) {
                tableView.getItems().add(seller);
            }

            addBtn.setOnAction(e -> showAddSellerDialog());

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            showAlert("Error", "Failed to load view: " + e.getMessage());
        }
    }

    @FXML
    private void manageProducts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/AdminProductsTable.fxml"));
            Parent view = loader.load();

            TableView<FurnitureItem> tableView = (TableView<FurnitureItem>) view.lookup("#tableView");
            Button discountBtn = (Button) view.lookup("#discountBtn");
            TableColumn<FurnitureItem, String> idCol = (TableColumn<FurnitureItem, String>) tableView.getColumns().get(0);
            TableColumn<FurnitureItem, String> nameCol = (TableColumn<FurnitureItem, String>) tableView.getColumns().get(1);
            TableColumn<FurnitureItem, String> typeCol = (TableColumn<FurnitureItem, String>) tableView.getColumns().get(2);
            TableColumn<FurnitureItem, String> priceCol = (TableColumn<FurnitureItem, String>) tableView.getColumns().get(3);
            TableColumn<FurnitureItem, String> stockCol = (TableColumn<FurnitureItem, String>) tableView.getColumns().get(4);
            TableColumn<FurnitureItem, String> discountCol = (TableColumn<FurnitureItem, String>) tableView.getColumns().get(5);

            idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getItemID())));
            nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
            priceCol.setCellValueFactory(data -> new SimpleStringProperty("$" + data.getValue().getPrice()));
            stockCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
            discountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue() instanceof Discountable ? "Yes" : "No"));

            List<FurnitureItem> items = currentAdmin.getAllItemsAcrossWarehouses();
            for (FurnitureItem item : items) {
                tableView.getItems().add(item);
            }

            discountBtn.setOnAction(e -> showApplyDiscountDialog());

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            showAlert("Error", "Failed to load view: " + e.getMessage());
        }
    }

    @FXML
    private void manageWarehouses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/final_project/AdminWarehousesTable.fxml"));
            Parent view = loader.load();

            TableView<Warehouse> tableView = (TableView<Warehouse>) view.lookup("#tableView");
            Button addBtn = (Button) view.lookup("#addBtn");
            TableColumn<Warehouse, String> locationCol = (TableColumn<Warehouse, String>) tableView.getColumns().get(0);
            TableColumn<Warehouse, String> itemsCol = (TableColumn<Warehouse, String>) tableView.getColumns().get(1);
            TableColumn<Warehouse, String> stockCol = (TableColumn<Warehouse, String>) tableView.getColumns().get(2);
            TableColumn<Warehouse, Void> actionsCol = (TableColumn<Warehouse, Void>) tableView.getColumns().get(3);

            locationCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
            itemsCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getInventoryCount())));
            stockCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTotalInventoryQuantity())));

            actionsCol.setCellFactory(column -> new TableCell<Warehouse, Void>() {
                private final Button deleteBtn = new Button("Delete");
                {
                    deleteBtn.setStyle(DELETE_BTN_STYLE);
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

            List<Warehouse> warehouses = currentAdmin.getAllWarehouses();
            for (Warehouse warehouse : warehouses) {
                tableView.getItems().add(warehouse);
            }

            addBtn.setOnAction(e -> showAddWarehouseDialog());

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            showAlert("Error", "Failed to load view: " + e.getMessage());
        }
    }

    private void showAddUserDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Add Customer");
        idDialog.setHeaderText("Enter Customer ID");
        Optional<String> idResult = idDialog.showAndWait();
        if (!idResult.isPresent()) return;

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Customer");
        nameDialog.setHeaderText("Enter Customer Name");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent()) return;

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Add Customer");
        emailDialog.setHeaderText("Enter Customer Email");
        Optional<String> emailResult = emailDialog.showAndWait();
        if (!emailResult.isPresent()) return;

        try {
            int id = Integer.parseInt(idResult.get());
            currentAdmin.addUser(id, nameResult.get(), emailResult.get());
            manageUsers();
            updateStats();
            showAlert("Success", "Customer added successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add customer: " + e.getMessage());
        }
    }

    private void showAddSellerDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Add Seller");
        idDialog.setHeaderText("Enter Seller ID");
        Optional<String> idResult = idDialog.showAndWait();
        if (!idResult.isPresent()) return;

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Seller");
        nameDialog.setHeaderText("Enter Seller Name");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent()) return;

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Add Seller");
        emailDialog.setHeaderText("Enter Seller Email");
        Optional<String> emailResult = emailDialog.showAndWait();
        if (!emailResult.isPresent()) return;

        try {
            int id = Integer.parseInt(idResult.get());
            currentAdmin.addSeller(id, nameResult.get(), emailResult.get());
            manageSellers();
            updateStats();
            showAlert("Success", "Seller added successfully!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add seller: " + e.getMessage());
        }
    }

    private void showAddWarehouseDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Warehouse");
        dialog.setHeaderText("Enter warehouse location");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                currentAdmin.addWarehouse(result.get());
                manageWarehouses();
                updateStats();
                showAlert("Success", "Warehouse added successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to add warehouse: " + e.getMessage());
            }
        }
    }

    private void showApplyDiscountDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Apply Discount");
        dialog.setHeaderText("Enter discount percentage");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                double discount = Double.parseDouble(result.get());
                int count = currentAdmin.applyGlobalDiscount(discount);
                manageProducts();
                showAlert("Success", "Discount applied to " + count + " items!");
            } catch (Exception e) {
                showAlert("Error", "Failed to apply discount: " + e.getMessage());
            }
        }
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setContentText("Delete " + user.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                currentAdmin.removeMember(user.getMemberId());
                manageUsers();
                updateStats();
                showAlert("Success", "Customer deleted!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete: " + e.getMessage());
            }
        }
    }

    private void deleteSeller(Seller seller) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Seller");
        alert.setContentText("Delete " + seller.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                currentAdmin.removeMember(seller.getMemberId());
                manageSellers();
                updateStats();
                showAlert("Success", "Seller deleted!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete: " + e.getMessage());
            }
        }
    }

    private void deleteWarehouse(Warehouse warehouse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Warehouse");
        alert.setContentText("Delete warehouse at " + warehouse.getLocation() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                currentAdmin.removeWarehouse(warehouse.getLocation());
                manageWarehouses();
                updateStats();
                showAlert("Success", "Warehouse deleted!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
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

            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }
}

