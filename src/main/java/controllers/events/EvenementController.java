package controllers.events;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import entity.Evenement;
import services.events.ServiceEvenement;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EvenementController {
    @FXML
    private TableView<Evenement> evenementTable;
    @FXML
    private TableColumn<Evenement, String> nomColumn;
    @FXML
    private TableColumn<Evenement, String> descriptionColumn;
    @FXML
    private TableColumn<Evenement, LocalDateTime> dateDebutColumn;
    @FXML
    private TableColumn<Evenement, LocalDateTime> dateFinColumn;
    @FXML
    private TableColumn<Evenement, String> lieuColumn;
    @FXML
    private TableColumn<Evenement, Integer> capaciteColumn;
    @FXML
    private TableColumn<Evenement, String> typeColumn;
    @FXML
    private TableColumn<Evenement, Double> prixColumn;
    @FXML
    private TableColumn<Evenement, String> organisateurColumn;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        // Initialize table columns with proper cell value factories
        nomColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNom()));
        descriptionColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDescription()));
        dateDebutColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDateDebut()));
        dateFinColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDateFin()));
        lieuColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLieu()));
        capaciteColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getCapacite()).asObject());
        typeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTypeEvenement()));
        prixColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());
        organisateurColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrganisateur()));

        // Load data
        loadEvenements();
    }

    private void loadEvenements() {
        try {
            List<Evenement> evenements = serviceEvenement.afficher();
            evenementTable.getItems().setAll(evenements);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des événements: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/AjouterEvenement.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            
            // Get the controller and set up any necessary initialization
            AjouterEvenementController controller = loader.getController();
            
            // Show the window and wait for it to close
            stage.showAndWait();
            
            // Refresh the table after the window is closed
            loadEvenements();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifier() {
        Evenement selectedEvenement = evenementTable.getSelectionModel().getSelectedItem();
        if (selectedEvenement == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/events/yasmine/ModifierEvenement.fxml"));
            Parent root = loader.load();
            
            ModifierEvenementController controller = loader.getController();
            controller.initData(selectedEvenement);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier un événement");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            loadEvenements(); // Refresh the table after modification
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimer() {
        Evenement selectedEvenement = evenementTable.getSelectionModel().getSelectedItem();
        if (selectedEvenement == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement à supprimer.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer l'événement '" + selectedEvenement.getNom() + "' ?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceEvenement.supprimer(selectedEvenement.getId());
                    loadEvenements(); // Refresh the table after deletion
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été supprimé avec succès.");
                } catch (Exception e) {
                    showAlert("Erreur", "Une erreur est survenue lors de la suppression: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 