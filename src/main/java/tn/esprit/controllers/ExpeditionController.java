package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Expedition;
import tn.esprit.services.ExpeditionService;

import java.io.IOException;
import java.time.LocalDate;

public class ExpeditionController {

    @FXML private TextField titreField;
    @FXML private TextField objectifField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextField aventurierIdField;
    @FXML private TextField videoUrlField;

    @FXML private ListView<String> expeditionListView;

    private final ExpeditionService expeditionService = new ExpeditionService();
    private ObservableList<Expedition> expeditionObservableList = FXCollections.observableArrayList();
    private Expedition selectedExpedition = null;

    @FXML
    public void initialize() {
        afficherExpeditions();

        expeditionListView.setOnMouseClicked(event -> {
            int index = expeditionListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < expeditionObservableList.size()) {
                selectedExpedition = expeditionObservableList.get(index);
                remplirChamps(selectedExpedition);
            }
        });
    }

    private void remplirChamps(Expedition expedition) {
        titreField.setText(expedition.getTitre());
        objectifField.setText(expedition.getObjectif());
        dateDebutPicker.setValue(expedition.getDateDebut());
        dateFinPicker.setValue(expedition.getDateFin());
        aventurierIdField.setText(String.valueOf(expedition.getAventurierId()));
        videoUrlField.setText(expedition.getVideoUrl());
    }

    @FXML
    private void onVoirAventurierClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/aventurier_view.fxml"));
            Parent root = fxmlLoader.load();

            // Option 1 : remplacer le contenu de la fenêtre actuelle
            titreField.getScene().setRoot(root);

            // Option 2 (alternative) : ouvrir dans une nouvelle fenêtre
            /*
            Stage stage = new Stage();
            stage.setTitle("Liste des Aventuriers");
            stage.setScene(new Scene(root));
            stage.show();
            */

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue des aventuriers.");
        }
    }

    @FXML
    public void ajouterExpedition() {
        if (titreField.getText().isEmpty() || objectifField.getText().isEmpty() ||
                dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null ||
                aventurierIdField.getText().isEmpty() || videoUrlField.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin.");
            return;
        }

        int aventurierId;
        try {
            aventurierId = Integer.parseInt(aventurierIdField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID de l'aventurier doit être un nombre entier.");
            return;
        }

        Expedition expedition = new Expedition(
                titreField.getText(),
                objectifField.getText(),
                dateDebutPicker.getValue(),
                dateFinPicker.getValue(),
                aventurierId,
                videoUrlField.getText()
        );

        expeditionService.addExpedition(expedition);
        clearFields();
        afficherExpeditions();
    }

    @FXML
    public void modifierExpedition() {
        if (selectedExpedition != null) {
            if (titreField.getText().isEmpty() || objectifField.getText().isEmpty() ||
                    dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null ||
                    aventurierIdField.getText().isEmpty() || videoUrlField.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
                showAlert("Erreur", "La date de début ne peut pas être après la date de fin.");
                return;
            }

            int aventurierId;
            try {
                aventurierId = Integer.parseInt(aventurierIdField.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID de l'aventurier doit être un nombre entier.");
                return;
            }

            selectedExpedition.setTitre(titreField.getText());
            selectedExpedition.setObjectif(objectifField.getText());
            selectedExpedition.setDateDebut(dateDebutPicker.getValue());
            selectedExpedition.setDateFin(dateFinPicker.getValue());
            selectedExpedition.setAventurierId(aventurierId);
            selectedExpedition.setVideoUrl(videoUrlField.getText());

            expeditionService.updateExpedition(selectedExpedition);
            clearFields();
            afficherExpeditions();
        }
    }

    @FXML
    public void supprimerExpedition() {
        if (selectedExpedition != null) {
            expeditionService.deleteExpedition(selectedExpedition.getId());
            clearFields();
            afficherExpeditions();
        }
    }

    private void afficherExpeditions() {
        expeditionObservableList.setAll(expeditionService.getAllExpeditions());
        ObservableList<String> expeditionStringList = FXCollections.observableArrayList();

        for (Expedition exp : expeditionObservableList) {
            expeditionStringList.add(exp.toString());
        }

        expeditionListView.setItems(expeditionStringList);
    }

    private void clearFields() {
        titreField.clear();
        objectifField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        aventurierIdField.clear();
        videoUrlField.clear();
        selectedExpedition = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
