package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    public void goToUsers(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org.example/user_view.fxml")); // Ajuste si besoin
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Utilisateurs");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToProfile(ActionEvent event) {
        loadScene(event, "/org.example/profile.fxml", "Mon Profil");
    }


    public void logout(ActionEvent event) {
        loadScene(event, "/org.example/login.fxml", "Connexion");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
