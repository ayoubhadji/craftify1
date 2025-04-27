package controllers.commande;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuCommandesController {
    
    @FXML
    private Label messageLabel;

    @FXML
    private void naviguerVersAjout() {
        naviguerVers("/org.example/commande/AjouterCommande.fxml");
    }

    @FXML
    private void naviguerVersModification() {
        naviguerVers("/org.example/commande/ModifierCommande.fxml");
    }

    @FXML
    private void naviguerVersSuppression() {
        naviguerVers("/org.example/commande/SupprimerCommande.fxml");
    }

    @FXML
    private void naviguerVersAffichage() {
        naviguerVers("/org.example/commande/AfficherCommande.fxml");
    }

    @FXML
    private void retourMenuPrincipal() {
        naviguerVers("/fxml/MenuPrincipal.fxml");
    }

    private void naviguerVers(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            messageLabel.setText("Erreur lors de la navigation : " + e.getMessage());
        }
    }
} 