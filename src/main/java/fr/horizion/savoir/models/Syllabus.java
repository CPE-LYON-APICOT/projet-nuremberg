package fr.horizion.savoir.models;

import java.util.List;

public class Syllabus {

    private int id;
    private List<String> chapitres;
    private List<String> objectifs;

    public Syllabus(int id, List<String> chapitres, List<String> objectifs) {
        this.id = id;
        this.chapitres = chapitres;
        this.objectifs = objectifs;
    }

    public int getId() {
        return id;
    }

    public List<String> getChapitres() {
        return chapitres;
    }
    public List<String> getObjectifs() {
        return objectifs;
    }

    public void setChapitres(List<String> chapitres) {
        this.chapitres = chapitres;
    }

    public void setObjectifs(List<String> objectifs) {
        this.objectifs = objectifs;
    }

    public void addChapitre(String chapitre) {
        this.chapitres.add(chapitre);
    }

    public void addObjectif(String objectif) {
        this.objectifs.add(objectif);
    }
}
