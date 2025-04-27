package services.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class StripePaymentService {

    public StripePaymentService() {
        System.out.println("Initialisation du service Stripe");
        Stripe.apiKey = ConfigLoader.getStripeSecretKey();
    }

    public Map<String, String> createSetupIntent() throws StripeException {
        System.out.println("Création d'un SetupIntent");
        SetupIntent setupIntent = SetupIntent.create(new HashMap<>());

        Map<String, String> result = new HashMap<>();
        result.put("client_secret", setupIntent.getClientSecret());
        result.put("public_key", ConfigLoader.getStripePublicKey());

        System.out.println("SetupIntent créé avec succès - ID: " + setupIntent.getId());
        return result;
    }

    public String createPaymentIntent(double amount, String currency) throws StripeException {
        // Convert amount to cents (Stripe uses smallest currency unit)
        long amountInCents = (long) (amount * 100);

        System.out.println("Création d'un PaymentIntent - Montant: " + amountInCents + " centimes " + currency);

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amountInCents);
        params.put("currency", currency);
        params.put("payment_method_types", new String[]{"card"});
        params.put("setup_future_usage", "off_session");

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        System.out.println("PaymentIntent créé avec succès - ID: " + paymentIntent.getId());
        return paymentIntent.getClientSecret();
    }

    public PaymentIntent confirmPayment(String paymentIntentId, String paymentMethodId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", paymentMethodId);

        return paymentIntent.confirm(params);
    }
}