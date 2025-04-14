package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BackController {

    public void goToUsers(ActionEvent event) {
        loadScene(event, "/org/example/user_view.fxml", "Gestion des Utilisateurs");
    }

    public void goToDashboard(ActionEvent event) {
        loadScene(event, "/org/example/dashboard.fxml", "Dashboard");
    }

    public void goToProducts(ActionEvent event) {
        loadScene(event, "/org/example/products.fxml", "Produits");
    }

    public void logout(ActionEvent event) {
        loadScene(event, "/org/example/login.fxml", "Connexion");
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
