package controllers.produit;

import entity.Produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.produit.ProduitService;
import services.produit.ProduitServiceImpl;

import java.io.IOException;

public class ModifierProduitController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField imageUrlField;
    @FXML
    private Label messageLabel;

    private final ProduitService produitService;

    public ModifierProduitController() {
        this.produitService = new ProduitServiceImpl();
    }

    @FXML
    private void handleModifier() {
        try {
            validateFields();
            
            int id = Integer.parseInt(idField.getText());
            Produit existingProduit = produitService.getProduitById(id);
            
            if (existingProduit == null) {
                showError("Produit non trouvé avec l'ID : " + id);
                return;
            }

            existingProduit.setNom(nomField.getText());
            existingProduit.setDescription(descriptionField.getText());
            existingProduit.setPrix(Double.parseDouble(prixField.getText()));
            existingProduit.setStock(Integer.parseInt(stockField.getText()));
            if (imageUrlField.getText() != null && !imageUrlField.getText().isEmpty()) {
                existingProduit.setImageUrl(imageUrlField.getText());
            }

            produitService.updateProduit(existingProduit);
            showSuccess("Produit modifié avec succès !");
            clearFields();
        } catch (NumberFormatException e) {
            showError("Veuillez entrer des valeurs numériques valides pour l'ID, le prix et le stock.");
        } catch (Exception e) {
            showError("Une erreur est survenue : " + e.getMessage());
        }
    }

    private void validateFields() throws IllegalArgumentException {
        if (idField.getText().isEmpty()) throw new IllegalArgumentException("L'ID est requis");
        if (nomField.getText().isEmpty()) throw new IllegalArgumentException("Le nom est requis");
        if (descriptionField.getText().isEmpty()) throw new IllegalArgumentException("La description est requise");
        if (prixField.getText().isEmpty()) throw new IllegalArgumentException("Le prix est requis");
        if (stockField.getText().isEmpty()) throw new IllegalArgumentException("Le stock est requis");
        
        try {
            Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID doit être un nombre entier");
        }
        
        try {
            Double.parseDouble(prixField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le prix doit être un nombre valide");
        }
        
        try {
            Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le stock doit être un nombre entier");
        }
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
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
            showError("Erreur lors du retour à la page précédente : " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.clear();
        nomField.clear();
        descriptionField.clear();
        prixField.clear();
        stockField.clear();
        imageUrlField.clear();
    }

    public void setProduit(Produit produit) {
        if (produit != null) {
            idField.setText(String.valueOf(produit.getId()));
            nomField.setText(produit.getNom());
            descriptionField.setText(produit.getDescription());
            prixField.setText(String.valueOf(produit.getPrix()));
            stockField.setText(String.valueOf(produit.getStock()));
            imageUrlField.setText(produit.getImageUrl());
        }
    }
} 