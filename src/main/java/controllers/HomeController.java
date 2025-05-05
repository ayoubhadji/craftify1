package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    public void goToProfile(ActionEvent event) {
        loadScene(event, "/org.example/user/profile.fxml", "Mon Profil");
    }
    public void logout(ActionEvent event) {
        loadScene(event, "/org.example/user/login.fxml", "Connexion");
    }
    public void goHome(ActionEvent event) {
        loadScene(event, "/org.example/home.fxml", "Accueil");
    }
    public void chatbot(ActionEvent event) {
        loadScene(event, "/org.example/chatbot_view.fxml", "chatbot");
    }
    public void goprudect(ActionEvent event) { loadScene(event, "/org.example/produit/AfficherProduitFront.fxml", "produit");
    }
    public void gopost(ActionEvent event) { loadScene(event, "/org.example/blog/Post.fxml", "blog");
    }
    public void gotoevents(ActionEvent event) { loadScene(event, "/org.example/events/yasmine/AfficherEvenementFront.fxml", "events");

    }
    public void gotofoire(ActionEvent event) { loadScene(event, "/org.example/foire/ViewFoire2.fxml", "foire");
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
