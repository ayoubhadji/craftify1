package controllers.commande;

import DAO.UserDAO;
import entity.Commande;
import entity.User;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AfficherCommandeController implements Initializable {
    @FXML private ComboBox<User> clientFilterComboBox;
    @FXML private ComboBox<String> statutFilterComboBox;
    @FXML private TableView<Commande> commandesTable;
    @FXML private TableColumn<Commande, Integer> idColumn;
    @FXML private TableColumn<Commande, String> dateColumn;
    @FXML private TableColumn<Commande, String> clientColumn;
    @FXML private TableColumn<Commande, String> statutColumn;
    @FXML private TableColumn<Commande, Double> totalColumn;
    @FXML private Label messageLabel;

    private final CommandeService commandeService;
    private final UserDAO userDAO;
    private final ObservableList<Commande> commandes;
    private FilteredList<Commande> filteredCommandes;

    public AfficherCommandeController() {
        this.commandeService = new CommandeServiceImpl();
        this.userDAO = new UserDAO();
        this.commandes = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFilters();
        setupCommandesTable();
        setupListeners();
        loadCommandes();
    }

    private void setupFilters() {
        ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAll());
        clientFilterComboBox.setItems(users);
        clientFilterComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getNom() + " (" + user.getEmail() + ")" : "Tous les utilisateurs";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });

        statutFilterComboBox.setItems(FXCollections.observableArrayList(
                "Tous les statuts", "EN_ATTENTE", "EN_COURS", "TERMINEE", "ANNULEE"
        ));
        statutFilterComboBox.setValue("Tous les statuts");
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

        filteredCommandes = new FilteredList<>(commandes);
        commandesTable.setItems(filteredCommandes);
    }

    private void setupListeners() {
        clientFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        statutFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter());
    }

    private void loadCommandes() {
        commandes.clear();
        commandes.addAll(commandeService.getAllCommandes());
    }

    private void updateFilter() {
        filteredCommandes.setPredicate(commande -> {
            User selectedUser = clientFilterComboBox.getValue();
            boolean matchClient = selectedUser == null || selectedUser.getId() == commande.getIdClient();
            boolean matchStatut = statutFilterComboBox.getValue().equals("Tous les statuts") ||
                    statutFilterComboBox.getValue().equals(commande.getStatut());
            return matchClient && matchStatut;
        });
    }

    @FXML
    private void handleResetFiltres() {
        clientFilterComboBox.setValue(null);
        statutFilterComboBox.setValue("Tous les statuts");
    }

    @FXML
    private void handleRafraichir() {
        loadCommandes();
        messageLabel.setText("Données rafraîchies");
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
