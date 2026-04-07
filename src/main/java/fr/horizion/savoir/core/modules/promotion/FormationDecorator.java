package fr.horizion.savoir.core.modules.promotion;

import fr.horizion.savoir.models.formation.Formartion;

public abstract class FormationDecorator extends Formartion {

    protected Formartion wrappedFormation;

    public FormationDecorator(Formartion wrappedFormation) {
        super(
            wrappedFormation.getId(),
            wrappedFormation.getTitre(),
            wrappedFormation.getDescription(),
            wrappedFormation.getPrix(),
            wrappedFormation.getLesEleves(),
            wrappedFormation.getContenuePedagogiques()
        );
        this.wrappedFormation = wrappedFormation;
    }

    public abstract float getPrice();
}

