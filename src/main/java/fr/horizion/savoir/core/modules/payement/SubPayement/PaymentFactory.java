package fr.horizion.savoir.core.modules.payement.SubPayement;


import fr.horizion.savoir.core.modules.payement.PayementStrategy;
import fr.horizion.savoir.core.modules.payement.SubPayement.PayPalStrategy;
import fr.horizion.savoir.core.modules.payement.SubPayement.StripeStrategy;

public class PaymentFactory {

    public PayementStrategy createPaymentStrategy(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de paiement ne peut pas être nul");
        }

        return switch (type.toLowerCase()) {
            case "stripe" -> new StripeStrategy();
            case "paypal" -> new PayPalStrategy();
            default -> throw new IllegalArgumentException("Méthode de paiement inconnue : " + type);
        };
    }
}
