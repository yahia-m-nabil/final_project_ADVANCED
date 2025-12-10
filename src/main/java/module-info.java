module org.example.final_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.final_project to javafx.fxml;
    exports org.example.final_project;
}