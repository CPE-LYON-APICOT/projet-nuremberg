package fr.horizion.savoir.models;

public class ContenuePedagoique  {

    private String titre;
    private Boolean estTermine;

    public ContenuePedagoique(String titre, Boolean estTermine) {
        this.titre = titre;
        this.estTermine = estTermine;
    }


    public void affiche() {
        System.out.println(titre);
    }
}
