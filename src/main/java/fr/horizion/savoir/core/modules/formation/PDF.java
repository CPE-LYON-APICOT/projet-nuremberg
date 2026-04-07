package fr.horizion.savoir.core.modules.formation;

import fr.horizion.savoir.models.formation.ContenuePedagogique;

public class PDF extends ContenuePedagogique {

    private int nbPage;
    public PDF(String titre, Boolean estTermine, int nbPage ) {
        super(titre, estTermine);
        this.nbPage = nbPage;
    }

    public int getNbPage() {
        return nbPage;
    }
}
