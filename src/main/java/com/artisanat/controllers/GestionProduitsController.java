package com.artisanat.controllers;

import com.artisanat.entities.Produit;
import com.artisanat.services.ProduitService;
import com.artisanat.services.impl.ProduitServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class GestionProduitsController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private TextField imageUrlField;
    @FXML private TableView<Produit> produitsTable;
    @FXML private TableColumn<Produit, Integer> idCol;
    @FXML private TableColumn<Produit, String> nomCol;
    @FXML private TableColumn<Produit, String> descriptionCol;
    @FXML private TableColumn<Produit, Double> prixCol;
    @FXML private TableColumn<Produit, Integer> stockCol;
    @FXML private TableColumn<Produit, String> imageUrlCol;
    @FXML private TableColumn<Produit, LocalDateTime> dateCreationCol;
    @FXML private TableColumn<Produit, LocalDateTime> dateModificationCol;

    private ProduitService produitService;
    private ObservableList<Produit> produitsList;
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$");

    @FXML
    public void initialize() {
        produitService = new ProduitServiceImpl();

        // Initialiser les colonnes de la table
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        prixCol.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
        stockCol.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        imageUrlCol.setCellValueFactory(cellData -> cellData.getValue().imageUrlProperty());
        dateCreationCol.setCellValueFactory(cellData -> cellData.getValue().dateCreationProperty());
        dateModificationCol.setCellValueFactory(cellData -> cellData.getValue().dateModificationProperty());

        // Configurer la sélection de la table
        produitsTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    afficherProduit(newValue);
                } else {
                    clearFields();
                }
            }
        );

        // Configurer le tri des colonnes
        produitsTable.getSortOrder().addListener((ListChangeListener<TableColumn<Produit, ?>>) change -> {
            while (change.next()) {
                if (!change.getList().isEmpty()) {
                    produitsTable.sort();
                }
            }
        });

        // Charger les données initiales
        chargerProduits();
    }

    private void afficherProduit(Produit produit) {
        if (produit != null) {
            nomField.setText(produit.getNom());
            descriptionField.setText(produit.getDescription());
            prixField.setText(String.format("%.2f", produit.getPrix()));
            stockField.setText(String.valueOf(produit.getStock()));
            imageUrlField.setText(produit.getImageUrl());
        }
    }

    private void chargerProduits() {
        produitsList = FXCollections.observableArrayList(produitService.getAllProduits());
        produitsTable.setItems(produitsList);
        if (!produitsTable.getSortOrder().isEmpty()) {
            produitsTable.sort();
        }
    }

    @FXML
    private void ajouterProduit() {
        try {
            if (!validerChamps()) {
                return;
            }

            Produit produit = new Produit(
                nomField.getText().trim(),
                descriptionField.getText().trim(),
                Double.parseDouble(prixField.getText()),
                Integer.parseInt(stockField.getText()),
                imageUrlField.getText().trim()
            );

            produitService.ajouterProduit(produit);
            chargerProduits();
            clearFields();
            showAlert("Succès", "Le produit a été ajouté avec succès", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir des valeurs numériques valides pour le prix et le stock", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierProduit() {
        Produit selectedProduit = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduit != null) {
            try {
                if (!validerChamps()) {
                    return;
                }

                selectedProduit.setNom(nomField.getText().trim());
                selectedProduit.setDescription(descriptionField.getText().trim());
                selectedProduit.setPrix(Double.parseDouble(prixField.getText()));
                selectedProduit.setStock(Integer.parseInt(stockField.getText()));
                selectedProduit.setImageUrl(imageUrlField.getText().trim());
                selectedProduit.setDateModification(LocalDateTime.now());

                produitService.modifierProduit(selectedProduit);
                chargerProduits();
                clearFields();
                showAlert("Succès", "Le produit a été modifié avec succès", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez saisir des valeurs numériques valides pour le prix et le stock", Alert.AlertType.ERROR);
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un produit à modifier", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void supprimerProduit() {
        Produit selectedProduit = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduit != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer le produit : " + selectedProduit.getNom() + " ?");
            
            if (confirmation.showAndWait().get() == ButtonType.OK) {
                produitService.supprimerProduit(selectedProduit.getId());
                chargerProduits();
                clearFields();
                showAlert("Succès", "Le produit a été supprimé avec succès", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un produit à supprimer", Alert.AlertType.WARNING);
        }
    }

    private boolean validerChamps() {
        // Validation du nom
        if (nomField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom du produit est obligatoire", Alert.AlertType.ERROR);
            return false;
        }
        if (nomField.getText().trim().length() < 3) {
            showAlert("Erreur", "Le nom du produit doit contenir au moins 3 caractères", Alert.AlertType.ERROR);
            return false;
        }

        // Validation du prix
        if (prixField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le prix est obligatoire", Alert.AlertType.ERROR);
            return false;
        }
        try {
            double prix = Double.parseDouble(prixField.getText());
            if (prix <= 0) {
                showAlert("Erreur", "Le prix doit être supérieur à 0", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide", Alert.AlertType.ERROR);
            return false;
        }

        // Validation du stock
        if (stockField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le stock est obligatoire", Alert.AlertType.ERROR);
            return false;
        }
        try {
            int stock = Integer.parseInt(stockField.getText());
            if (stock < 0) {
                showAlert("Erreur", "Le stock ne peut pas être négatif", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le stock doit être un nombre entier valide", Alert.AlertType.ERROR);
            return false;
        }

        // Validation de l'URL de l'image (optionnelle)
        String imageUrl = imageUrlField.getText().trim();
        if (!imageUrl.isEmpty() && !URL_PATTERN.matcher(imageUrl).matches()) {
            showAlert("Erreur", "L'URL de l'image n'est pas valide", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void clearFields() {
        nomField.clear();
        descriptionField.clear();
        prixField.clear();
        stockField.clear();
        imageUrlField.clear();
        produitsTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 