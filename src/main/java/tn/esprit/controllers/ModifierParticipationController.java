package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.entities.Participation;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceParticipation;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifierParticipationController implements Initializable {
    @FXML
    private ComboBox<Integer> userIdComboBox;
    @FXML
    private ComboBox<Evenement> evenementComboBox;
    @FXML
    private TextField prixField;
    @FXML
    private ComboBox<String> statutComboBox;

    private ServiceParticipation serviceParticipation;
    private ServiceEvenement serviceEvenement;
    private Participation participation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceParticipation = new ServiceParticipation();
        serviceEvenement = new ServiceEvenement();

        // Load user IDs and events into combo boxes
        ObservableList<Integer> userIds = FXCollections.observableArrayList(1, 2, 3, 4, 5); // Example user IDs
        ObservableList<Evenement> evenements = FXCollections.observableArrayList(serviceEvenement.afficher());

        userIdComboBox.setItems(userIds);
        evenementComboBox.setItems(evenements);
    }

    public void setParticipation(Participation participation) {
        this.participation = participation;
        updateFields();
    }

    private void updateFields() {
        if (participation != null) {
            userIdComboBox.setValue(participation.getUserId());
            evenementComboBox.setValue(participation.getEvenement());
            prixField.setText(String.valueOf(participation.getPrix()));
            statutComboBox.setValue(participation.getStatut());
        }
    }

    @FXML
    private void handleModifier() {
        try {
            Integer selectedUserId = userIdComboBox.getValue();
            Evenement selectedEvenement = evenementComboBox.getValue();
            double prix = Double.parseDouble(prixField.getText());
            String statut = statutComboBox.getValue();

            if (selectedUserId == null || selectedEvenement == null || statut == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            participation.setUserId(selectedUserId);
            participation.setEvenement(selectedEvenement);
            participation.setPrix(prix);
            participation.setStatut(statut);

            serviceParticipation.modifier(participation);

            // Close the window
            Stage stage = (Stage) userIdComboBox.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide");
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) userIdComboBox.getScene().getWindow();
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