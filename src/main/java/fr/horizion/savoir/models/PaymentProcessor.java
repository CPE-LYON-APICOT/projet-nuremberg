package fr.horizion.savoir.models;

import fr.horizion.savoir.core.modules.payement.PayementStrategy;

public class PaymentProcessor {
    private final PayementStrategy strategy;

    // Constructeur qui accepte la stratégie
    public PaymentProcessor(PayementStrategy strategy) {
        this.strategy = strategy;
    }

    // On utilise long, String, String pour matcher l'interface
    public boolean process(long amount, String currency, String orderId) {
        if (strategy == null) return false;
        return strategy.pay(amount, currency, orderId);
    }
}