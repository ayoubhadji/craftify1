package tn.esprit.controllers;

import tn.esprit.entities.Expedition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.services.ExpeditionService;

import java.sql.SQLException;

public class AddExpeditionController {

    @FXML private TextField titreField;
    @FXML private TextField objectifField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextField videoUrlField;

    private ExpeditionService service = new ExpeditionService();

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

            // Créer l'expédition sans aventurier
            Expedition e = new Expedition(
                    titreField.getText(),
                    objectifField.getText(),
                    dateDebutPicker.getValue(),
                    dateFinPicker.getValue(),
                    -1, // Pas d'aventurier sélectionné, mettre à -1
                    videoUrlField.getText()
            );

            // Ajouter l'expédition à la base de données
            service.addExpedition(e);

            // Fermeture de la fenêtre
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.close();

            // Affichage d'un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Expédition ajoutée avec succès!");

        } catch (SQLException e) {
            e.printStackTrace();
            // Affichage d'un message d'erreur détaillé
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout de l'expédition. Veuillez réessayer.");
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Fermeture de la fenêtre sans sauvegarder
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    // Méthode pour afficher des alertes personnalisées
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}
