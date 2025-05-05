package controllers.payment;

import com.stripe.exception.StripeException;
import entity.Commande;
import entity.Panier;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import services.payment.StripePaymentService;
import services.commande.CommandeService;
import services.commande.CommandeServiceImpl;
import controllers.commande.DetailsCommandeController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StripePaymentController {

    @FXML
    private WebView cardElement;

    @FXML
    private Label statusLabel;

    @FXML
    private Button payButton;

    @FXML
    private Button voirCommandeButton;

    private double amount;
    private List<Panier> panierItems;
    private StripePaymentService stripeService;
    private WebEngine webEngine;
    private boolean stripeReady = false;
    private Commande currentCommande;
    private final CommandeService commandeService = new CommandeServiceImpl();

    @FXML
    public void initialize() {
        stripeService = new StripePaymentService();
        webEngine = cardElement.getEngine();

        System.out.println("Initialisation du contrôleur de paiement");
        
        // Désactiver le bouton de paiement jusqu'à ce que Stripe soit prêt
        payButton.setDisable(true);
        statusLabel.setText("Initialisation du formulaire de paiement...");

        String url = getClass().getResource("/org.example/payment/stripe-elements.html").toExternalForm();
        System.out.println("Chargement de l'URL: " + url);
        webEngine.load(url);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Page web chargée avec succès");

                // Ajouter un délai pour s'assurer que la page est complètement chargée
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("paymentCallback", new PaymentCallback(this));
                    System.out.println("Interface JavaScript enregistrée");
                    initializeStripe();
                });
                delay.play();
            }
        });

        // Le bouton "Voir Commande" est toujours disponible
        if (voirCommandeButton != null) {
            voirCommandeButton.setDisable(false);
        }
    }

    private void initializeStripe() {
        try {
            Map<String, String> setupData = stripeService.createSetupIntent();
            String publicKey = setupData.get("public_key");
            System.out.println("Clé publique récupérée: " + publicKey);
            
            String script = String.format(
                    "window.postMessage({type: 'stripeInit', publicKey: '%s'}, '*');",
                    publicKey
            );
            System.out.println("Exécution du script d'initialisation");
            webEngine.executeScript(script);
        } catch (StripeException e) {
            System.err.println("Erreur lors de l'initialisation de Stripe: " + e.getMessage());
            handleError("Erreur d'initialisation: " + e.getMessage());
        }
    }

    public void setAmount(double amount) {
        this.amount = amount;
            Platform.runLater(() -> {
                if (statusLabel != null) {
                    statusLabel.setText(String.format("Montant à payer: %.2f €", amount));
                }
            });
    }

    public void setPanierItems(List<Panier> items) {
        this.panierItems = items;
    }

    public void setCommande(Commande commande) {
        this.currentCommande = commande;
    }

    @FXML
    private void handlePayment() {
        if (!stripeReady) {
            handleError("Le formulaire n'est pas encore prêt. Veuillez patienter...");
            return;
        }

        try {
            System.out.println("Début du processus de paiement");
            payButton.setDisable(true);
            statusLabel.setText("Traitement du paiement en cours...");
            statusLabel.setStyle("-fx-text-fill: black;");

            // Simuler le paiement
            if (stripeService.simulatePayment(amount, "eur")) {
                handleSuccess();
            } else {
                handleError("Le paiement a échoué");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du paiement: " + e.getMessage());
            e.printStackTrace();
            handleError("Erreur technique: " + e.getMessage());
            payButton.setDisable(false);
        }
    }

    private void handleError(String message) {
        System.err.println("Erreur de paiement: " + message);
        Platform.runLater(() -> {
            statusLabel.setText("Erreur: " + message);
            statusLabel.setStyle("-fx-text-fill: red;");
            payButton.setDisable(false);
        });
    }

    private void handleSuccess() {
        System.out.println("Paiement réussi!");
        Platform.runLater(() -> {
            statusLabel.setText("Paiement réussi !");
            statusLabel.setStyle("-fx-text-fill: green;");
            payButton.setDisable(true);
            
            if (currentCommande != null) {
                currentCommande.setStatut("PAYEE");
                commandeService.update(currentCommande);
            }

            if (panierItems != null) {
                panierItems.clear();
            }

            // Attendre 2 secondes avant de fermer la fenêtre
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                Stage stage = (Stage) statusLabel.getScene().getWindow();
                stage.close();
            });
            delay.play();
        });
    }

    @FXML
    private void cancelPayment() {
        if (currentCommande != null) {
            currentCommande.setStatut("ANNULEE");
            commandeService.update(currentCommande);
        }
        Stage stage = (Stage) payButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void voirDetailsCommande() {
        System.out.println("Clic sur le bouton Voir Commande");
        if (currentCommande == null) {
            System.err.println("Erreur : currentCommande est null");
            statusLabel.setText("Erreur : Impossible de trouver la commande");
            return;
        }
        
        System.out.println("Affichage des détails de la commande #" + currentCommande.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.example/commande/DetailsCommande.fxml"));
            Parent root = loader.load();

            DetailsCommandeController detailsController = loader.getController();
            detailsController.setCommande(currentCommande);

            Stage stage = (Stage) voirCommandeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la commande #" + currentCommande.getId());
            System.out.println("Navigation vers l'interface des détails réussie");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'affichage des détails : " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Erreur lors de l'affichage des détails");
        }
    }

    public void retourhome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org.example/home.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            handleError("Erreur lors du retour à l'accueil");
        }
    }

    public void handlePaymentCallback(String type, String message) {
        System.out.println("Message reçu -> Type: " + type + ", Message: " + message);

        if ("info".equals(type)) {
            if (message.contains("Stripe prêt")) {
                stripeReady = true;
                payButton.setDisable(false);
                statusLabel.setText("Formulaire prêt pour le paiement");
                statusLabel.setStyle("-fx-text-fill: green;");
                System.out.println("Formulaire prêt pour le paiement.");
            }
        } else if ("success".equals(type)) {
            System.out.println("🎉 Paiement réussi !");
            handleSuccess();
        } else if ("error".equals(type)) {
            System.err.println("Erreur : " + message);
            handleError(message);
        }
    }
}
