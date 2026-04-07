package fr.horizion.savoir.core.modules.formation;

import fr.horizion.savoir.models.formation.ContenuePedagogique;

public class Video extends ContenuePedagogique {

    private int duree;

    public Video(String titre, Boolean estTermine, int duree) {
        super(titre, estTermine);
        this.duree = duree;

    }
}
