package fr.horizion.savoir.core.modules.formation;

import fr.horizion.savoir.models.formation.ContenuePedagogique;

public class Quiz extends ContenuePedagogique {

    private int scoreMin;


    public Quiz(String titre, Boolean estTermine, int scoreMin) {
        super(titre, estTermine);
        this.scoreMin = scoreMin;
    }

    public int getScoreMin() {
        return scoreMin;
    }

    public void setScoreMin(int scoreMin) {
        this.scoreMin = scoreMin;
    }
}
