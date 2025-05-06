package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Expedition;
import tn.esprit.services.ExpeditionService;

import java.sql.SQLException;

public class EditExpeditionController {

    @FXML private TextField titreField;
    @FXML private TextField objectifField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextField videoUrlField;

    private final ExpeditionService expeditionService = new ExpeditionService();
    private Expedition expedition;

    // Méthode pour récupérer l'expédition à modifier
    public void setExpedition(Expedition expedition) {
        this.expedition = expedition;
        titreField.setText(expedition.getTitre());
        objectifField.setText(expedition.getObjectif());
        dateDebutPicker.setValue(expedition.getDateDebut());
        dateFinPicker.setValue(expedition.getDateFin());
        videoUrlField.setText(expedition.getVideoUrl());
    }

    @FXML
    void handleSave(ActionEvent event) {
        try {
            // Vérifier si tous les champs sont remplis
            if (titreField.getText().isEmpty() || objectifField.getText().isEmpty() ||
                    dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.");
                return;
            }

            // Vérifier que la date de début est avant la date de fin
            if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
                showAlert(Alert.AlertType.ERROR, "La date de début ne peut pas être après la date de fin.");
                return;
            }

            // Validation de l'URL de la vidéo (si elle est renseignée)
            if (!videoUrlField.getText().isEmpty() && !videoUrlField.getText().matches("^(https?|ftp)://.*$")) {
                showAlert(Alert.AlertType.ERROR, "L'URL de la vidéo n'est pas valide.");
                return;
            }

            // Validation des caractères pour le titre et l'objectif (pas de caractères spéciaux)
            if (!titreField.getText().matches("^[a-zA-Z0-9\\s]+$")) {
                showAlert(Alert.AlertType.ERROR, "Le titre ne peut contenir que des lettres et des chiffres.");
                return;
            }

            if (!objectifField.getText().matches("^[a-zA-Z0-9\\s]+$")) {
                showAlert(Alert.AlertType.ERROR, "L'objectif ne peut contenir que des lettres et des chiffres.");
                return;
            }

            // Mettre à jour l'expédition avec les nouvelles valeurs
            expedition.setTitre(titreField.getText());
            expedition.setObjectif(objectifField.getText());
            expedition.setDateDebut(dateDebutPicker.getValue());
            expedition.setDateFin(dateFinPicker.getValue());
            expedition.setVideoUrl(videoUrlField.getText());

            // Sauvegarder l'expédition mise à jour avec le service
            expeditionService.updateExpedition(expedition);

            // Fermer la fenêtre
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.close();

            // Affichage d'un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Expédition mise à jour avec succès!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour de l'expédition. Veuillez réessayer.");
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Fermer la fenêtre sans enregistrer
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    // Méthode pour afficher des alertes personnalisées
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}
