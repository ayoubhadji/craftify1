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

public class AjouterParticipationController implements Initializable {
    @FXML
    private ComboBox<Integer> userComboBox;
    @FXML
    private ComboBox<Evenement> evenementComboBox;
    @FXML
    private TextField prixField;
    @FXML
    private ComboBox<String> statutComboBox;

    private ServiceParticipation serviceParticipation;
    private ServiceEvenement serviceEvenement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceParticipation = new ServiceParticipation();
        serviceEvenement = new ServiceEvenement();

        // Initialize status options
        ObservableList<String> statuts = FXCollections.observableArrayList(
                "En attente",
                "Confirmée",
                "Annulée"
        );
        statutComboBox.setItems(statuts);

        // Load events into combo box
        ObservableList<Evenement> evenements = FXCollections.observableArrayList(serviceEvenement.afficher());
        evenementComboBox.setItems(evenements);

        // Set cell factory for event ComboBox to show event names
        evenementComboBox.setCellFactory(listView -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement evenement, boolean empty) {
                super.updateItem(evenement, empty);
                if (empty || evenement == null) {
                    setText(null);
                } else {
                    setText(evenement.getNom());
                }
            }
        });

        // Load user IDs (you should replace this with actual user data from your database)
        ObservableList<Integer> userIds = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        userComboBox.setItems(userIds);
    }

    @FXML
    private void handleAjouter() {
        try {
            Integer selectedUserId = userComboBox.getValue();
            Evenement selectedEvenement = evenementComboBox.getValue();
            double prix = Double.parseDouble(prixField.getText());
            String statut = statutComboBox.getValue();

            if (selectedUserId == null || selectedEvenement == null || statut == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            Participation participation = new Participation();
            participation.setUserId(selectedUserId);
            participation.setEvenement(selectedEvenement);
            participation.setPrix(prix);
            participation.setStatut(statut);

            serviceParticipation.ajouter(participation);

            // Close the window
            Stage stage = (Stage) userComboBox.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide");
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) userComboBox.getScene().getWindow();
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