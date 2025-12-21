package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.final_project.model.*;

import java.io.IOException;

public class SignupController {

    // --- FXML Injections ---
    @FXML
    private ToggleButton roleCustomer;
    @FXML
    private ToggleButton roleSeller;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private ToggleGroup roleGroup;

    /**
     * Initializes the controller. Sets up the ToggleGroup for role selection.
     */
    @FXML
    public void initialize() {
        // Group the toggles so only one can be selected
        roleGroup = new ToggleGroup();
        roleCustomer.setToggleGroup(roleGroup);
        roleSeller.setToggleGroup(roleGroup);

        // Ensure Customer is selected by default to match FXML
        roleCustomer.setSelected(true);

        System.out.println("Signup GUI Initialized");
    }

    /**
     * Logic for the Register Now button
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all fields.");
            return;
        }

        // Get the ECommerceSystem instance
        ECommerceSystem system = ECommerceSystem.getInstance();

        // Check if email already exists
        if (system.findMemberByEmail(email) != null) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "An account with this email already exists.");
            return;
        }

        try {
            // Generate a unique ID (using total member count + 100 to avoid conflicts with startup data)
            int newId = system.getAllMembers().size() + 100;

            // Ensure the ID is unique
            while (system.findMemberById(newId) != null) {
                newId++;
            }

            // Create the member based on selected role
            Member newMember;
            if (roleSeller.isSelected()) {
                newMember = system.signupSeller(name, email, password, newId);
                System.out.println("Seller registered successfully: " + name + " (ID: " + newId + ")");
            } else {
                newMember = system.signupUser(name, email, password, newId);
                System.out.println("User registered successfully: " + name + " (ID: " + newId + ")");
            }

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Account created successfully!\nYour ID is: " + newId + "\nPlease use your email to login.");

            // Navigate to login page
            navigateToLogin(event);

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "System Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Switches back to the Login view
     */
    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/final_project/Login.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();

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
            showError("Navigation Error", "Could not open login page: " + e.getMessage());
        }
    }

    /**
     * Helper method to show messages to the user
     */

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



