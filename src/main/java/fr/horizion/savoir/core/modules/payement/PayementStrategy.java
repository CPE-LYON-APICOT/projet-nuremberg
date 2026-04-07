package fr.horizion.savoir.core.modules.payement;

public interface PayementStrategy {
    boolean pay(long amount, String currency, String orderId);


}