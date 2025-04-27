package controllers.payment;

import com.stripe.exception.StripeException;
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
import java.util.List;
import java.util.Map;

public class StripePaymentController {

    @FXML
    private WebView cardElement;

    @FXML
    private Label statusLabel;

    @FXML
    private Button payButton;

    private double amount;
    private String clientSecret;
    private List<Panier> panierItems;
    private StripePaymentService stripeService;
    private WebEngine webEngine;

    @FXML
    public void initialize() {
        stripeService = new StripePaymentService();
        webEngine = cardElement.getEngine();

        // Log pour le débogage
        System.out.println("Initialisation du contrôleur de paiement");

        // Load the Stripe Elements HTML
        String url = getClass().getResource("/org.example/payment/stripe-elements.html").toExternalForm();
        System.out.println("Chargement de l'URL: " + url);
        webEngine.load(url);

        // Wait for the page to load
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Page web chargée avec succès");

                // Add the JavaScript interface
                JSObject window = (JSObject) webEngine.executeScript("window");
                PaymentCallback callback = new PaymentCallback(this);  // Pass controller instance
                window.setMember("paymentCallback", callback);  // This is where the PaymentCallback is registered

                System.out.println("Interface JavaScript enregistrée");

                // Initialize Stripe with the public key and client secret
                if (clientSecret != null) {
                    System.out.println("Initialisation des éléments Stripe avec clientSecret");
                    initializeStripeElements();
                } else {
                    System.out.println("Pas de clientSecret disponible pour l'instant");
                }
            }
        });
    }

    private void initializeStripeElements() {
        try {
            if (clientSecret == null) {
                handleError("Client secret is missing!");
                return;
            }

            Map<String, String> setupData = stripeService.createSetupIntent();
            String script = String.format(
                    "window.postMessage({type: 'stripeInit', publicKey: '%s', clientSecret: '%s'}, '*');",
                    setupData.get("public_key"),
                    clientSecret
            );
            webEngine.executeScript(script);
        } catch (StripeException e) {
            handleError("Erreur d'initialisation Stripe: " + e.getMessage());
        }
    }

    public void setAmount(double amount) {
        this.amount = amount;
        try {
            clientSecret = stripeService.createPaymentIntent(amount, "eur");
            Platform.runLater(() -> {
                if (statusLabel != null) {
                    statusLabel.setText(String.format("Montant à payer: %.2f €", amount));
                }
                if (webEngine != null && webEngine.getLoadWorker().getState() == Worker.State.SUCCEEDED) {
                    initializeStripeElements();
                }
            });
        } catch (StripeException e) {
            handleError("Erreur lors de la création du paiement: " + e.getMessage());
        }
    }

    public void setPanierItems(List<Panier> items) {
        this.panierItems = items;
    }

    @FXML
    private void handlePayment() {
        if (clientSecret == null) {
            handleError("Erreur: Paiement non initialisé");
            return;
        }

        try {
            System.out.println("Début du processus de paiement");
            payButton.setDisable(true);
            statusLabel.setText("Traitement du paiement en cours...");
            statusLabel.setStyle("-fx-text-fill: black;");

            // Appel de la nouvelle fonction JavaScript
            System.out.println("Appel de la fonction startPayment");
            webEngine.executeScript("window.startPayment()");
        } catch (Exception e) {
            System.err.println("Erreur lors du déclenchement du paiement: " + e.getMessage());
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

            if (panierItems != null) {
                panierItems.clear();
            }

            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));
            pauseTransition.setOnFinished(e -> {
                Stage stage = (Stage) payButton.getScene().getWindow();
                stage.close();
            });
            pauseTransition.play();
        });
    }

    @FXML
    private void cancelPayment() {
        Stage stage = (Stage) payButton.getScene().getWindow();
        stage.close();
    }

    public void retourhome(ActionEvent event) {
        loadScene(event, "/org.example/home.fxml", "Accueil");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlePaymentCallback(String status, String message) {
        if ("info".equals(status)) {
            System.out.println("Information reçue : " + message);
            return;
        }

        if ("succeeded".equals(status)) {
            System.out.println("Paiement réussi !");
            handleSuccess(); // <<< ici on déclenche ton succès visuellement
        } else if ("failed".equals(status)) {
            System.out.println("Paiement échoué : " + message);
            handleError(message); // <<< ici on déclenche ton affichage d'erreur
        } else {
            System.out.println("Statut de paiement inconnu : " + status);
        }
    }

}
