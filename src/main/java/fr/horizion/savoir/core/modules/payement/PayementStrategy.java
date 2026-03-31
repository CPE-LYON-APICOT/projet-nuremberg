package fr.horizion.savoir.core.modules.payement;

public interface PayementStrategy {

    boolean pay(float amount);
}
