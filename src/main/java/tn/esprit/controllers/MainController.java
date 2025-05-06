package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    void handleExpeditionView() throws IOException {
        // Charger la vue de gestion des expéditions
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/expedition_view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestion des Expéditions");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void handleAventurierView() throws IOException {
        // Charger la vue de gestion des aventuriers
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/aventurier_view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestion des Aventuriers");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
