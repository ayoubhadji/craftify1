package controllers.commande;

import DAO.UserDAO;
import entity.Commande;
import entity.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;
import services.payment.StripePaymentService;
import controllers.payment.StripePaymentController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Map;

public class AjouterCommandeController implements Initializable {

    @FXML private ComboBox<User> clientComboBox;
    @FXML private TableView<?> lignesTable;
    @FXML private Label totalLabel;
    @FXML private Label messageLabel;
    @FXML private Button commanderButton;

    private final CommandeService commandeService = new CommandeServiceImpl();
    private final UserDAO userDAO = new UserDAO();
    private final StripePaymentService stripeService = new StripePaymentService();
    private Commande currentCommande;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialisation du contrôleur");
        setupClientComboBox();
        // Pour test, on met un total fixe
        totalLabel.setText("100.00 €");
    }

    private void setupClientComboBox() {
        ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAll());
        clientComboBox.setItems(users);
        clientComboBox.setConverter(new javafx.util.StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getNom() + " (" + user.getEmail() + ")" : "";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleAjouterCommande() {
        System.out.println("Début du processus de commande");
        messageLabel.setText("Traitement de la commande...");

        try {
            User client = clientComboBox.getValue();
            if (client == null) {
                messageLabel.setText("Veuillez sélectionner un client");
                return;
            }

            // Récupérer le total depuis le label
            String totalText = totalLabel.getText().replaceAll("[^0-9.]", "");
            System.out.println("Total extrait : " + totalText);
            messageLabel.setText("Création du paiement...");

            double total = Double.parseDouble(totalText);
            System.out.println("Création de l'intention de paiement pour " + total + "€");

            // Créer l'intention de paiement Stripe
            Map<String, String> paymentIntent = stripeService.createPaymentIntent(total, "eur");
            String clientSecret = paymentIntent.get("client_secret");
            System.out.println("Intention de paiement créée avec succès. Secret: " + clientSecret);
            messageLabel.setText("Paiement créé, ouverture du formulaire...");

            // Sauvegarder la commande
            currentCommande = new Commande();
            currentCommande.setIdClient(client.getId());
            currentCommande.setDateCommande(LocalDateTime.now());
            currentCommande.setStatut("EN_ATTENTE");
            currentCommande.setTotal(total);
            commandeService.save(currentCommande);
            System.out.println("Commande sauvegardée avec succès, ID: " + currentCommande.getId());

            // Ouvrir le formulaire de paiement
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/payment/stripe-payment.fxml"));
                    Parent root = loader.load();

                    StripePaymentController paymentController = loader.getController();
                    paymentController.setAmount(total);
                    paymentController.setCommande(currentCommande); // Passer la commande au contrôleur de paiement

                    Stage paymentStage = new Stage();
                    paymentStage.initModality(Modality.APPLICATION_MODAL);
                    paymentStage.setTitle("Paiement sécurisé");
                    paymentStage.setScene(new Scene(root));

                    paymentStage.show();
                    System.out.println("Fenêtre de paiement affichée");
                    messageLabel.setText("Formulaire de paiement prêt");

                } catch (IOException e) {
                    System.err.println("Erreur lors de l'affichage du formulaire : " + e.getMessage());
                    e.printStackTrace();
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            });

        } catch (NumberFormatException e) {
            System.err.println("Erreur de format de nombre : " + e.getMessage());
            messageLabel.setText("Erreur : Le total n'est pas valide - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur générale : " + e.getMessage());
            messageLabel.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
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
            messageLabel.setText("Erreur lors du retour au menu");
        }
    }
}
