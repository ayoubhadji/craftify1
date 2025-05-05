package controllers.payment;

public class PaymentCallback {

    private final StripePaymentController stripePaymentController;

    // Constructeur
    public PaymentCallback(StripePaymentController stripePaymentController) {
        this.stripePaymentController = stripePaymentController;
    }

    // Appelé par Stripe HTML
    public void onMessage(String type, String message) {
        stripePaymentController.handlePaymentCallback(type, message);
    }
}
