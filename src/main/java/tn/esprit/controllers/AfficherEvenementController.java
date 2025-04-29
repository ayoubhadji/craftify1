package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AfficherEvenementController implements Initializable {

    @FXML
    private ListView<Evenement> evenementListView;

    @FXML
    private TextField searchField;

    @FXML
    private TabPane mainTabPane;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListView();
        loadEvenementData();
        setupSearchListener();
    }

    private void setupListView() {
        evenementListView.setCellFactory(listView -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement evenement, boolean empty) {
                super.updateItem(evenement, empty);
                if (empty || evenement == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // Format the display text with event details
                    String displayText = String.format("Nom: %s | Lieu: %s | Type: %s | Date: %s - %s | Prix: %.2f DT",
                            evenement.getNom(),
                            evenement.getLieu(),
                            evenement.getTypeEvenement(),
                            evenement.getDateDebut().format(dateFormatter),
                            evenement.getDateFin().format(dateFormatter),
                            evenement.getPrix());
                    setText(displayText);

                    // Add some styling
                    setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                }
            }
        });
    }

    private void loadEvenementData() {
        evenementList.clear();
        evenementList.addAll(serviceEvenement.afficher());
        evenementListView.setItems(evenementList);
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                evenementListView.setItems(evenementList);
            } else {
                ObservableList<Evenement> filteredList = FXCollections.observableArrayList();
                for (Evenement evenement : evenementList) {
                    if (evenement.getNom().toLowerCase().contains(newValue.toLowerCase()) ||
                            evenement.getLieu().toLowerCase().contains(newValue.toLowerCase()) ||
                            evenement.getTypeEvenement().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(evenement);
                    }
                }
                evenementListView.setItems(filteredList);
            }
        });
    }

    @FXML
    void handleSupprimer(ActionEvent event) {
        Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer l'événement");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                serviceEvenement.supprimer(selectedEvenement.getId());
                loadEvenementData();
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
    }

    @FXML
    void handleModifier(ActionEvent event) {
        Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/yasmine/ModifierEvenement.fxml"));
                Parent root = loader.load();
                ModifierEvenementController controller = loader.getController();
                controller.initData(selectedEvenement);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Événement");
                stage.showAndWait();
                loadEvenementData();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/yasmine/AjouterEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Événement");
            stage.showAndWait();
            loadEvenementData();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    @FXML
    void handleDetails(ActionEvent event) {
        Evenement selectedEvenement = evenementListView.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/yasmine/DetailsEvenement.fxml"));
                Parent root = loader.load();
                DetailsEvenementController controller = loader.getController();
                controller.setEvenement(selectedEvenement);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'événement");
                stage.show();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre des détails.");
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement pour voir les détails.");
        }
    }

    @FXML
    private void handleVoirParticipations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/yasmine/AfficherParticipation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}