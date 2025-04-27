package controllers.produit;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.produit.SupprimerProduitServiceImpl;
import services.produit.SupprimerProduitService;

import java.io.IOException;

public class SupprimerProduitController {
    
    private final SupprimerProduitService supprimerProduitService = (SupprimerProduitService) new SupprimerProduitServiceImpl();
    
    @FXML
    private TextField idField;
    @FXML
    private Label messageLabel;
    
    @FXML
    private void handleSupprimerProduit() {
        try {
            int id = Integer.parseInt(idField.getText());
            
            supprimerProduitService.supprimerProduit(id);
            
            showAlert(AlertType.INFORMATION, "Succès", "Le produit a été supprimé avec succès.");
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer un ID valide.");
        }
    }
    
    private void clearFields() {
        idField.clear();
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/produit/GestionProduits.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            messageLabel.setText("Erreur lors du retour à la page précédente : " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}