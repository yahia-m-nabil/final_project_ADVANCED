package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

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
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Determine selected role
        String role = "Customer";
        if (roleSeller.isSelected()) {
            role = "Seller";
        }

        // --- Logic Placeholders ---

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all fields.");
            return;
        }

        System.out.println("Attempting to register " + role + ": " + name + " (" + email + ")");

        // TODO: Call your Authentication class or MembersList
        // Example:
        // boolean success = membersList.register(name, email, password, role);

        // if (success) {
        //    showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
        //    navigateToLogin();
        // }
    }

    /**
     * Switches back to the Login view
     */
    @FXML
    private void navigateToLogin(ActionEvent event) {
        // TODO: Load LoginView.fxml and set to Stage
        try {
            // 1. Load the signup FXML
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/final_project/Login.fxml"));

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



