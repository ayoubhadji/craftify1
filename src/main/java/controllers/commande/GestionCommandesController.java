package controllers.commande;


import entity.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;

public class GestionCommandesController {
    @FXML private TextField clientIdField;
    @FXML private ComboBox<String> statutCombo;
    @FXML private TextField totalField;
    @FXML private TableView<Commande> commandeTable;
    @FXML private TableColumn<Commande, Integer> idColumn;
    @FXML private TableColumn<Commande, LocalDateTime> dateCommandeColumn;
    @FXML private TableColumn<Commande, String> statutColumn;
    @FXML private TableColumn<Commande, Double> totalColumn;
    @FXML private TableColumn<Commande, Integer> clientIdColumn;

    private CommandeService commandeService;
    private ObservableList<Commande> commandeList;

    @FXML
    public void initialize() {
        commandeService = new CommandeServiceImpl();
        
        // Initialiser les statuts
        ObservableList<String> statuts = FXCollections.observableArrayList(
            "EN_ATTENTE", "CONFIRMEE", "EN_PREPARATION", "EXPEDIEE", "LIVREE", "ANNULEE"
        );
        statutCombo.setItems(statuts);

        // Configurer les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("idClient"));

        // Charger les données
        refreshTable();

        // Configurer l'écouteur de sélection
        commandeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clientIdField.setText(String.valueOf(newSelection.getIdClient()));
                statutCombo.setValue(newSelection.getStatut());
                totalField.setText(String.valueOf(newSelection.getTotal()));
            }
        });
    }

    private void refreshTable() {
        commandeList = FXCollections.observableArrayList(commandeService.getAllCommandes());
        commandeTable.setItems(commandeList);
    }

    @FXML
    private void handleAjouter() {
        try {
            Commande commande = new Commande();
            commande.setIdClient(Integer.parseInt(clientIdField.getText()));
            commande.setStatut(statutCombo.getValue());
            commande.setTotal(Double.parseDouble(totalField.getText()));
            commande.setDateCommande(LocalDateTime.now());

            commandeService.save(commande);
            refreshTable();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commande ajoutée avec succès");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de la commande: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Commande selectedCommande = commandeTable.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une commande à modifier");
            return;
        }

        try {
            selectedCommande.setIdClient(Integer.parseInt(clientIdField.getText()));
            selectedCommande.setStatut(statutCombo.getValue());
            selectedCommande.setTotal(Double.parseDouble(totalField.getText()));

            commandeService.update(selectedCommande);
            refreshTable();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commande modifiée avec succès");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de la commande: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        Commande selectedCommande = commandeTable.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une commande à supprimer");
            return;
        }

        try {
            commandeService.delete(selectedCommande.getId());
            refreshTable();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commande supprimée avec succès");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de la commande: " + e.getMessage());
        }
    }

    @FXML
    private void handleEffacer() {
        clearFields();
    }

    private void clearFields() {
        clientIdField.clear();
        statutCombo.getSelectionModel().clearSelection();
        totalField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
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