package controllers.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entity.Participation;
import services.events.ServiceParticipation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AfficherParticipationController implements Initializable {

    @FXML
    private ListView<Participation> participationListView;

    @FXML
    private TextField searchField;

    @FXML
    private TabPane mainTabPane;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    private ObservableList<Participation> participationList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListView();
        loadParticipationData();
        setupSearchListener();
    }

    private void setupListView() {
        participationListView.setCellFactory(listView -> new ListCell<Participation>() {
            @Override
            protected void updateItem(Participation participation, boolean empty) {
                super.updateItem(participation, empty);
                if (empty || participation == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // Format the display text with event name, price, and status
                    String displayText = String.format("Événement: %s | Prix: %.2f DT | Statut: %s",
                            participation.getEvenement().getNom(),
                            participation.getPrix(),
                            participation.getStatut());
                    setText(displayText);
                    
                    // Add some styling
                    setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                }
            }
        });
    }

    private void loadParticipationData() {
        participationList.clear();
        participationList.addAll(serviceParticipation.afficher());
        participationListView.setItems(participationList);
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                participationListView.setItems(participationList);
            } else {
                ObservableList<Participation> filteredList = FXCollections.observableArrayList();
                for (Participation participation : participationList) {
                    if (participation.getEvenement().getNom().toLowerCase().contains(newValue.toLowerCase()) ||
                        String.valueOf(participation.getPrix()).contains(newValue) ||
                        participation.getStatut().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(participation);
                    }
                }
                participationListView.setItems(filteredList);
            }
        });
    }

    @FXML
    void handleSupprimer(ActionEvent event) {
        Participation selectedParticipation = participationListView.getSelectionModel().getSelectedItem();
        if (selectedParticipation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la participation");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette participation ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                serviceParticipation.supprimer(selectedParticipation.getId());
                loadParticipationData();
            }
        } else {
            showAlert("Aucune participation sélectionnée", "Veuillez sélectionner une participation à supprimer.");
        }
    }

    @FXML
    void handleModifier(ActionEvent event) {
        Participation selectedParticipation = participationListView.getSelectionModel().getSelectedItem();
        if (selectedParticipation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/ModifierParticipation.fxml"));
                Parent root = loader.load();
                ModifierParticipationController controller = loader.getController();
                controller.setParticipation(selectedParticipation);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Participation");
                stage.showAndWait();
                loadParticipationData();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
            }
        } else {
            showAlert("Aucune participation sélectionnée", "Veuillez sélectionner une participation à modifier.");
        }
    }

    @FXML
    void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/AjouterParticipation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Participation");
            stage.showAndWait();
            loadParticipationData();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    @FXML
    void handleDetails(ActionEvent event) {
        Participation selectedParticipation = participationListView.getSelectionModel().getSelectedItem();
        if (selectedParticipation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/DetailsParticipation.fxml"));
                Parent root = loader.load();
                DetailsParticipationController controller = loader.getController();
                controller.setParticipation(selectedParticipation);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de la participation");
                stage.show();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre des détails.");
            }
        } else {
            showAlert("Aucune participation sélectionnée", "Veuillez sélectionner une participation pour voir les détails.");
        }
    }

    @FXML
        private void handleRetourEvenements(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/AfficherEvenement.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
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

    @FXML
    private void retourDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}