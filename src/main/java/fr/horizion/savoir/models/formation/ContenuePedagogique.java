package fr.horizion.savoir.models.formation;

public abstract class ContenuePedagogique {

    private String titre;
    private Boolean estTermine;

    public ContenuePedagogique(String titre, Boolean estTermine) {
        this.titre = titre;
        this.estTermine = estTermine;
    }


    public String getTitre() {
        return titre;
    }

    public Boolean getEstTermine() {
        return estTermine;
    }

    public boolean isTermine() {
        return Boolean.TRUE.equals(estTermine);
    }

    public void marquerTermine() {
        this.estTermine = true;
    }


    public boolean affiche() {
        System.out.println(titre);
        return true;
    }

    @Override
    public String toString() {
        return "ContenuePedagogique{" +
                "titre='" + titre + '\'' +
                ", estTermine=" + estTermine +
                '}';
    }
}
