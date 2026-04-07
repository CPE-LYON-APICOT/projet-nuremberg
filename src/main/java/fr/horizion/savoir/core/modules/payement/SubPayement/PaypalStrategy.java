package fr.horizion.savoir.core.modules.payement.SubPayement;

import fr.horizion.savoir.core.modules.payement.PayementStrategy;

abstract public class PaypalStrategy implements PayementStrategy {

    @Override
    public boolean pay(float amount) {
        return false;
    }


}
