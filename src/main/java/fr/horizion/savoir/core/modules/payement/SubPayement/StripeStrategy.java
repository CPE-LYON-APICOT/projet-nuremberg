package fr.horizion.savoir.core.modules.payement.SubPayement;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import fr.horizion.savoir.core.modules.payement.PayementStrategy;
import java.util.logging.Logger;

public class StripeStrategy implements PayementStrategy {
    private static final Logger LOGGER = Logger.getLogger(StripeStrategy.class.getName());
    private final StripeClient stripeClient;
    private final boolean configured;

    public StripeStrategy() {
        String apiKey = System.getenv("STRIPE_SECRET_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            LOGGER.severe("STRIPE_SECRET_KEY non définie — les paiements Stripe échoueront");
            this.stripeClient = null;
            this.configured = false;
        } else {
            this.stripeClient = new StripeClient(apiKey);
            this.configured = true;
        }
    }

    @Override
    public boolean pay(long amount, String currency, String orderId) {
        if (!configured || stripeClient == null) {
            LOGGER.severe("Stripe non configuré, paiement refusé pour " + orderId);
            return false;
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency(currency.toLowerCase())
                    .putMetadata("order_id", orderId)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent intent = stripeClient.paymentIntents().create(params);

            LOGGER.info("Paiement Stripe initié pour la commande " + orderId + " (ID: " + intent.getId() + ")");

            return "succeeded".equals(intent.getStatus()) || "requires_action".equals(intent.getStatus());

        } catch (StripeException e) {
            LOGGER.severe("Erreur Stripe lors du paiement de la commande " + orderId + " : " + e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.severe("Erreur inattendue lors du paiement Stripe de la commande " + orderId + " : " + e.getMessage());
            return false;
        }
    }
}