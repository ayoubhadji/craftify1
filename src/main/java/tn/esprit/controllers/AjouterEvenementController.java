package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjouterEvenementController {
    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TextField lieuField;
    @FXML
    private TextField capaciteField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField organisateurField;
    @FXML
    private TextField imageField;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private Evenement evenementToModify;

    public void initialize() {
        // Initialize any other necessary components
    }

    public void setEvenementToModify(Evenement evenement) {
        this.evenementToModify = evenement;
        if (evenement != null) {
            nomField.setText(evenement.getNom());
            descriptionField.setText(evenement.getDescription());
            dateDebutPicker.setValue(evenement.getDateDebut().toLocalDate());
            dateFinPicker.setValue(evenement.getDateFin().toLocalDate());
            lieuField.setText(evenement.getLieu());
            capaciteField.setText(String.valueOf(evenement.getCapacite()));
            typeField.setText(evenement.getTypeEvenement());
            prixField.setText(String.valueOf(evenement.getPrix()));
            organisateurField.setText(evenement.getOrganisateur());
            imageField.setText(evenement.getImage());
        }
    }

    @FXML
    private void handleChoisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            // Validate required fields
            if (nomField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null ||
                    lieuField.getText().isEmpty() || capaciteField.getText().isEmpty() ||
                    typeField.getText().isEmpty() || prixField.getText().isEmpty() ||
                    organisateurField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Create new event
            Evenement evenement = new Evenement();
            evenement.setNom(nomField.getText());
            evenement.setDescription(descriptionField.getText());
            evenement.setDateDebut(LocalDateTime.of(dateDebutPicker.getValue(), LocalTime.MIDNIGHT));
            evenement.setDateFin(LocalDateTime.of(dateFinPicker.getValue(), LocalTime.MIDNIGHT));
            evenement.setLieu(lieuField.getText());
            evenement.setCapacite(Integer.parseInt(capaciteField.getText()));
            evenement.setTypeEvenement(typeField.getText());
            evenement.setPrix(Double.parseDouble(prixField.getText()));
            evenement.setOrganisateur(organisateurField.getText());
            evenement.setImage(imageField.getText());
            evenement.setDateCreation(LocalDateTime.now());

            // Add event to database
            serviceEvenement.ajouter(evenement);

            // Close the window
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour la capacité et le prix.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'événement: " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}