module com.example.pidev2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires java.sql; // <-- Add this line for java.sql visibility

    opens com.example.pidev2 to javafx.fxml;
    opens com.example.pidev2.controllers to javafx.fxml;
    exports com.example.pidev2;
    exports com.example.pidev2.controllers;
}
