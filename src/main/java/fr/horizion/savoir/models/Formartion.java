package fr.horizion.savoir.models;

import java.util.List;
import java.util.Observer;

public class Formartion {

    private int id;
    private String titre;
    private String description;
    private float prix;
    private List<Observer> lesEleves;

    public Formartion(int id, String titre, String description, float prix, List<Observer> lesEleves) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.lesEleves = lesEleves;

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

    public List<Observer> getLesEleves() {
        return lesEleves;
    }
}
