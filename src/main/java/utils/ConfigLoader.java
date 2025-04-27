package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Properties stripeProperties;
    private static final String CONFIG_FILE = "/org.example/config/stripe.properties";

    static {
        loadStripeConfig();
    }

    private static void loadStripeConfig() {
        stripeProperties = new Properties();
        try (InputStream input = ConfigLoader.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Cannot find " + CONFIG_FILE);
                // Fallback to default test key if config file is not found

            }
            stripeProperties.load(input);
            System.out.println("Loaded Stripe configuration successfully");
        } catch (IOException e) {
            System.err.println("Error loading " + CONFIG_FILE + ": " + e.getMessage());
            // Fallback to default test key if there's an error
        }
    }

    public static String getStripeSecretKey() {
        return stripeProperties.getProperty("stripe.secret.key");
    }

    public static String getStripePublicKey() {
        return stripeProperties.getProperty("stripe.public.key");
    }
}
