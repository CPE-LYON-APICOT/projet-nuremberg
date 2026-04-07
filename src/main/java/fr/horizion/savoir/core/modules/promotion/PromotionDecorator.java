package fr.horizion.savoir.core.modules.promotion;

import fr.horizion.savoir.models.formation.Formartion;

public class PromotionDecorator extends FormationDecorator {

    private float remise;

    public PromotionDecorator(Formartion wrappedFormation, float remise) {
        super(wrappedFormation);
        this.remise = remise;
    }

    @Override
    public float getPrice() {
        return wrappedFormation.getPrix() - (wrappedFormation.getPrix() * remise / 100);
    }

    @Override
    public String toString() {
        return super.toString() + " (Remise: " + remise + "%)";
    }
}
