package fr.horizion.savoir.models;

import fr.horizion.savoir.core.modules.payement.PayementStrategy;

public class PaymentProcessor {


    private PayementStrategy payementStrategy;

     public PaymentProcessor(PayementStrategy payementStrategy) {
         this.payementStrategy = payementStrategy;
     }

     public boolean process(float amount) {
         return payementStrategy.pay(amount);
     }



}
