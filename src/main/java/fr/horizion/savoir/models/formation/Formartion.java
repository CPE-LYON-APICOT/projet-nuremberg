package fr.horizion.savoir.models.formation;

import fr.horizion.savoir.core.modules.formation.ContentFactory;
import fr.horizion.savoir.models.Etudiant;

import java.util.List;
import java.util.Observer;

public class Formartion {

    private int id;
    private String titre;
    private String description;
    private float prix;
    private final List<Etudiant> lesEleves;

    private List<ContenuePedagogique> contenuePedagogiques;

    public Formartion(int id, String titre, String description, float prix, List<Etudiant> lesEleves, List<ContenuePedagogique> contenuePedagogiques) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.lesEleves = lesEleves;
        this.contenuePedagogiques = contenuePedagogiques;

    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public List<Etudiant> getLesEleves() {
        return lesEleves;
    }


    private final ContentFactory contentFactory = new ContentFactory();

    public void ajouterContenu(String type, String titre, Boolean estTermine, int v) {
        ContenuePedagogique c = contentFactory.createContent(type, titre, estTermine, v);
        contenuePedagogiques.add(c);
    }

    public List<ContenuePedagogique> getContenuePedagogiques() {
        return contenuePedagogiques;
    }



    @Override
    public String toString() {
        return " titre:" + titre + " description:" + description + " prix:" + prix + " lesEleves:" + lesEleves + " contenuePedagogiques:" + contenuePedagogiques;
    }

}
