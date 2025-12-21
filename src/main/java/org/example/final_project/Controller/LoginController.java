package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.final_project.model.*; // Importing model classes

import java.io.IOException;

public class LoginController {

    // --- FXML Injections mapped to Login.fxml ---
    @FXML private ToggleButton roleCustomer;
    @FXML private ToggleButton roleSeller;
    @FXML private ToggleButton roleAdmin;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private ToggleGroup roleGroup;

    /**
     * Initializes the controller, setting up role selection logic.
     */
    @FXML
    public void initialize() {
        roleGroup = new ToggleGroup();
        roleCustomer.setToggleGroup(roleGroup);
        roleSeller.setToggleGroup(roleGroup);
        roleAdmin.setToggleGroup(roleGroup);

        // Default selection as per UI design
        roleCustomer.setSelected(true);
    }

    /**
     * Handles the login process by verifying credentials against the ECommerceSystem.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Missing Information", "Please enter both email and password.");
            return;
        }

        // 1. Find the member by email using the system instance
        ECommerceSystem system = ECommerceSystem.getInstance();
        Member member = system.findMemberByEmail(email);

        // 2. Validate credentials
        if (member != null && member.checkPassword(password)) {

            // 3. Verify that the member matches the selected role
            if (isValidRole(member)) {
                // Set the current member in the session
                system.setCurrentMember(member);
                System.out.println("Login successful for " + member.getName());
                redirectUser(member, event);
            } else {
                showError("Role Mismatch", "The account found does not match the selected role.");
            }
        } else {
            showError("Login Failed", "Invalid email or password.");
        }
    }

    /**
     * Checks if the found Member instance matches the UI selection.
     */
    private boolean isValidRole(Member member) {
        if (roleAdmin.isSelected()) return member instanceof Admin;
        if (roleSeller.isSelected()) return member instanceof Seller;
        if (roleCustomer.isSelected()) return member instanceof User;
        return false;
    }

    /**
     * Switches the scene to the appropriate dashboard.
     */
    private void redirectUser(Member member, ActionEvent event) {
        String fxmlFile = "";

        if (member instanceof Admin) fxmlFile = "/org/example/final_project/AdminDashboard.fxml";
        else if (member instanceof Seller) fxmlFile = "/org/example/final_project/sellerPage.fxml";
        else if (member instanceof User) fxmlFile = "/org/example/final_project/StoreView.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Preserve window dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            stage.setScene(new Scene(root));
            stage.setTitle("FurnitureApp - " + member.getName());

            // Restore window dimensions
            if (maximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
            }

            stage.show();

        } catch (IOException e) {
            showError("System Error", "Could not load the dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToSignup(ActionEvent event) {
        try {
            // 1. Load the signup FXML
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/final_project/signup.fxml"));

            // 2. Get the current Stage (window) using any UI element (like emailField)
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Preserve window dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            // 3. Set the new scene
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
            showError("Navigation Error", "Could not open signup page: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

