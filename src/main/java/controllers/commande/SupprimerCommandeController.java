package controllers.commande;


import entity.Commande;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SupprimerCommandeController implements Initializable {
    @FXML private TableView<Commande> commandesTable;
    @FXML private TableColumn<Commande, Integer> idColumn;
    @FXML private TableColumn<Commande, String> dateColumn;
    @FXML private TableColumn<Commande, String> clientColumn;
    @FXML private TableColumn<Commande, String> statutColumn;
    @FXML private TableColumn<Commande, Double> totalColumn;
    

    
    @FXML private Label messageLabel;

    private final CommandeService commandeService;
    private final ObservableList<Commande> commandes;


    public SupprimerCommandeController() {
        this.commandeService = new CommandeServiceImpl();
        this.commandes = FXCollections.observableArrayList();
    }



    private void setupCommandesTable() {
        idColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateCommande()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        clientColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty("Client #" + cellData.getValue().getIdClient()));
        statutColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatut()));
        totalColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        commandesTable.setItems(commandes);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}