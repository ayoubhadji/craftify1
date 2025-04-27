package controllers.commande;


import entity.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierCommandeController implements Initializable {
    @FXML private ComboBox<Commande> commandeComboBox;
    @FXML private Label dateLabel;
    @FXML private Label clientLabel;
    @FXML private ComboBox<String> statutComboBox;
    @FXML private Label totalLabel;
    @FXML private Label messageLabel;

    private final CommandeService commandeService;

    public ModifierCommandeController() {
        this.commandeService = new CommandeServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();

    }

    private void setupComboBoxes() {
        // Configuration du ComboBox des commandes
        ObservableList<Commande> commandes = FXCollections.observableArrayList(commandeService.getAllCommandes());
        commandeComboBox.setItems(commandes);
        commandeComboBox.setConverter(new StringConverter<Commande>() {
            @Override
            public String toString(Commande commande) {
                return commande != null ? "Commande #" + commande.getId() : "";
            }

            @Override
            public Commande fromString(String string) {
                return null;
            }
        });

        // Configuration du ComboBox des statuts
        statutComboBox.setItems(FXCollections.observableArrayList(
            "EN_ATTENTE", "EN_COURS", "TERMINEE", "ANNULEE"
        ));
    }




    @FXML
    private void handleModifierCommande() {
        Commande commande = commandeComboBox.getValue();
        if (commande == null) {
            messageLabel.setText("Veuillez sélectionner une commande");
            return;
        }

        try {
            commande.setStatut(statutComboBox.getValue());
            commandeService.update(commande);
            messageLabel.setText("Commande modifiée avec succès");
            retourMenuCommandes();
        } catch (Exception e) {
            messageLabel.setText("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void retourMenuCommandes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/commande/MenuCommandes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            messageLabel.setText("Erreur lors du retour au menu des commandes");
        }
    }
} 