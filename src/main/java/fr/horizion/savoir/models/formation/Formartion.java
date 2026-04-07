package fr.horizion.savoir.models.formation;

import fr.horizion.savoir.core.modules.formation.ContentFactory;
import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.shared.observer.Observer;
import fr.horizion.savoir.shared.observer.Subject;

import java.util.ArrayList;
import java.util.List;

public class Formartion implements Subject {

    private int id;
    private String titre;
    private String description;
    private float prix;
    private final List<Etudiant> lesEleves;
    private List<Observer> observers = new ArrayList<>();



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

    @Override
    public void attach(Observer observer) {
        observers.add(observer);

    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
