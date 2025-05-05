package services.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import java.util.HashMap;
import java.util.Map;

public class StripePaymentService {

    public StripePaymentService() {
        System.out.println("Initialisation du service Stripe");
        Stripe.apiKey = ConfigLoader.getStripeSecretKey();
    }

    public Map<String, String> createSetupIntent() throws StripeException {
        System.out.println("Création des données de configuration");
        Map<String, String> result = new HashMap<>();
        result.put("public_key", ConfigLoader.getStripePublicKey());
        return result;
    }

    public Map<String, String> createPaymentIntent(double amount, String currency) throws StripeException {
        System.out.println("Création de l'intention de paiement");
        Map<String, String> result = new HashMap<>();
        result.put("client_secret", "simulated_client_secret");
        return result;
    }

    public boolean simulatePayment(double amount, String currency) {
        // Simuler un délai de traitement
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Toujours retourner true pour simuler un paiement réussi
        return true;
    }
}