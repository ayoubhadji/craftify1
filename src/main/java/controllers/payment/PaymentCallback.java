package controllers.payment;

public class PaymentCallback {

    private final StripePaymentController stripePaymentController;

    // Constructor that accepts a StripePaymentController instance
    public PaymentCallback(StripePaymentController stripePaymentController) {
        this.stripePaymentController = stripePaymentController;
    }

    // Method to handle the payment callback and notify the controller
    public void handlePaymentCallback(String status, String message) {
        // Now, call the method from the controller
        stripePaymentController.handlePaymentCallback(status, message);
    }
}
