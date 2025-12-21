package org.example.final_project.Controller;

import org.example.final_project.model.Admin;

import javafx.fxml.FXML;

public class HelloController {
    @FXML
    private Admin admin = new Admin(1, "AdminName", "admin@example.com");
    @FXML
    protected void onHelloButtonClick() {

    }
}
