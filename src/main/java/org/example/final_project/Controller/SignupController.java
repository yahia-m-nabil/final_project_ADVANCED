package main.java.org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class SignupController {

    // --- FXML Injections ---
    @FXML private ToggleButton roleCustomer;
    @FXML private ToggleButton roleSeller;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

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
    private void navigateToLogin() {
        // TODO: Load LoginView.fxml and set to Stage
        System.out.println("Navigating back to Login screen...");
    }

    /**
     * Helper method to show messages to the user
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}