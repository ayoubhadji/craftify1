package controllers.commande;

import DAO.UserDAO;
import entity.Commande;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DetailsCommandeController implements Initializable {
    @FXML private Label numeroCommandeLabel;
    @FXML private Label dateLabel;
    @FXML private Label clientLabel;
    @FXML private Label statutLabel;
    @FXML private Label totalLabel;
    @FXML private TableView<?> detailsTable;

    private final CommandeService commandeService = new CommandeServiceImpl();
    private final UserDAO userDAO = new UserDAO();
    private Commande commande;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialisation du contrôleur des détails de commande");
        // Initialiser les labels avec des valeurs par défaut
        numeroCommandeLabel.setText("-");
        dateLabel.setText("-");
        clientLabel.setText("-");
        statutLabel.setText("-");
        totalLabel.setText("-");
    }

    public void setCommande(Commande commande) {
        System.out.println("Définition de la commande #" + (commande != null ? commande.getId() : "null"));
        this.commande = commande;
        afficherDetails();
    }

    private void afficherDetails() {
        if (commande != null) {
            System.out.println("Affichage des détails de la commande #" + commande.getId());
            numeroCommandeLabel.setText(String.valueOf(commande.getId()));
            dateLabel.setText(commande.getDateCommande().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            // Récupérer les informations du client
            User client = userDAO.getById(commande.getIdClient());
            if (client != null) {
                clientLabel.setText(client.getNom() + " (" + client.getEmail() + ")");
            } else {
                clientLabel.setText("Client #" + commande.getIdClient());
            }

            statutLabel.setText(commande.getStatut());
            totalLabel.setText(String.format("%.2f €", commande.getTotal()));
            System.out.println("Détails de la commande affichés avec succès");
        } else {
            System.err.println("Impossible d'afficher les détails : commande null");
        }
    }

    @FXML
    private void handleImprimer() {
        // TODO: Implémenter l'impression
        System.out.println("Impression des détails de la commande " + commande.getId());
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/commande/MenuCommandes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) numeroCommandeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("Retour au menu des commandes");
        } catch (IOException e) {
            System.err.println("Erreur lors du retour au menu : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
