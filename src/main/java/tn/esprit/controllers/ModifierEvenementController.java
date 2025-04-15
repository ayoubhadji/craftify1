package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ModifierEvenementController {

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

    private Evenement evenement;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    public void initData(Evenement evenement) {
        this.evenement = evenement;

        // Populate fields with event data
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

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            // Update the event object with new values
            evenement.setNom(nomField.getText());
            evenement.setDescription(descriptionField.getText());
            evenement.setDateDebut(LocalDateTime.of(dateDebutPicker.getValue(), LocalTime.now()));
            evenement.setDateFin(LocalDateTime.of(dateFinPicker.getValue(), LocalTime.now()));
            evenement.setLieu(lieuField.getText());
            evenement.setCapacite(Integer.parseInt(capaciteField.getText()));
            evenement.setTypeEvenement(typeField.getText());
            evenement.setPrix(Double.parseDouble(prixField.getText()));
            evenement.setOrganisateur(organisateurField.getText());
            evenement.setImage(imageField.getText());

            // Save the updated event
            serviceEvenement.modifier(evenement);

            // Show success message and close the window
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été modifié avec succès.");
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs numériques valides pour la capacité et le prix.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (nomField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null ||
                lieuField.getText().isEmpty() || capaciteField.getText().isEmpty() ||
                typeField.getText().isEmpty() || prixField.getText().isEmpty() ||
                organisateurField.getText().isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
            return false;
        }

        try {
            int capacite = Integer.parseInt(capaciteField.getText());
            if (capacite <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La capacité doit être un nombre positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La capacité doit être un nombre entier valide.");
            return false;
        }

        try {
            double prix = Double.parseDouble(prixField.getText());
            if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix ne peut pas être négatif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre valide.");
            return false;
        }

        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        if (dateFin.isBefore(dateDebut)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de fin ne peut pas être antérieure à la date de début.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}