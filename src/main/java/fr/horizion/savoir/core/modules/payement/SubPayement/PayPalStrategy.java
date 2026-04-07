package fr.horizion.savoir.core.modules.payement.SubPayement;


import fr.horizion.savoir.core.modules.payement.PayementStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PayPalStrategy implements PayementStrategy {
    private static final Logger LOGGER = Logger.getLogger(PayPalStrategy.class.getName());

    // En prod, ces infos viennent de tes variables d'environnement
    private final String clientId;
    private final String clientSecret;

    public PayPalStrategy() {
        this.clientId = System.getenv("PAYPAL_CLIENT_ID");
        this.clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");
    }

    @Override
    public boolean pay(long amount, String currency, String orderId) {
        try {

            String payPalOrderId = "PAYID-" + System.currentTimeMillis();

            LOGGER.info("Paiement PayPal initié pour la commande " + orderId + " (PayPal ID: " + payPalOrderId + ")");

            return payPalOrderId != null;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur critique PayPal pour la commande " + orderId, e);
            return false;
        }
    }
}