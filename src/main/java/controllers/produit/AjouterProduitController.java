package controllers.produit;

import entity.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.produit.ProduitService;
import services.produit.ProduitServiceImpl;

import java.io.File;
import java.io.IOException;

public class AjouterProduitController {

    private final ProduitService produitService = new ProduitServiceImpl();

    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private TextField artisanIdField;
    @FXML private Label messageLabel;
    @FXML private Label imageFileNameLabel;

    private File selectedImageFile;

    @FXML
    private void handleAjouter() {
        try {
            validateFields();

            String nom = nomField.getText();
            String description = descriptionField.getText();
            double prix = Double.parseDouble(prixField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int artisanId = Integer.parseInt(artisanIdField.getText());

            // Vérifie si une image a été sélectionnée
            if (selectedImageFile == null) {
                showError("Veuillez sélectionner une image.");
                return;
            }

            String imagePath = selectedImageFile.toURI().toString();
            Produit produit = new Produit(nom, description, prix, stock, imagePath, artisanId);
            
            produitService.addProduit(produit);
            showSuccess("Produit ajouté avec succès !");
            clearFields();
        } catch (NumberFormatException e) {
            showError("Prix, stock ou ID artisan invalide.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }

    private void validateFields() throws IllegalArgumentException {
        if (nomField.getText().isEmpty()) {
            throw new IllegalArgumentException("Le nom est requis");
        }
        if (descriptionField.getText().isEmpty()) {
            throw new IllegalArgumentException("La description est requise");
        }
        if (prixField.getText().isEmpty()) {
            throw new IllegalArgumentException("Le prix est requis");
        }
        if (stockField.getText().isEmpty()) {
            throw new IllegalArgumentException("Le stock est requis");
        }
        if (artisanIdField.getText().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'artisan est requis");
        }

        try {
            double prix = Double.parseDouble(prixField.getText());
            if (prix <= 0) {
                throw new IllegalArgumentException("Le prix doit être supérieur à 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le prix doit être un nombre valide");
        }

        try {
            int stock = Integer.parseInt(stockField.getText());
            if (stock < 0) {
                throw new IllegalArgumentException("Le stock ne peut pas être négatif");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le stock doit être un nombre entier");
        }

        try {
            Integer.parseInt(artisanIdField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID de l'artisan doit être un nombre entier");
        }
    }

    private void showSuccess(String message) {
        messageLabel.setText("✅ " + message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    private void showError(String message) {
        messageLabel.setText("❌ " + message);
        messageLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML
    private void handleChoisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            imageFileNameLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/produit/GestionProduits.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du retour à la page précédente : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomField.clear();
        descriptionField.clear();
        prixField.clear();
        stockField.clear();
        artisanIdField.clear();
        imageFileNameLabel.setText("Aucun fichier choisi");
        selectedImageFile = null;
    }
}
