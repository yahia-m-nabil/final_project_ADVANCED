package org.example.final_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Use a leading slash to search from the root of the resources folder
        URL fxmlLocation = getClass().getResource("/org/example/final_project/Login.fxml");

        if (fxmlLocation == null) {
            throw new RuntimeException("Fatal Error: Could not find Login.fxml. Ensure it is in src/main/resources/org/example/final_project/");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Furniture Management System");
        stage.show();
    }
}