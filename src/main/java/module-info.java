module org.example.final_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.final_project to javafx.fxml;
    exports org.example.final_project;
    exports org.example.final_project.Controller;
    opens org.example.final_project.Controller to javafx.fxml;
    exports org.example.final_project.model;
    opens org.example.final_project.model to javafx.fxml;
}