package fr.horizion.savoir.core.modules.formation;

import fr.horizion.savoir.models.Syllabus;

public class SyllabusBuilder {

    private Syllabus syllabus;

    public SyllabusBuilder(Syllabus syllabus) {
        this.syllabus = syllabus;

    }

    public SyllabusBuilder addChapitre(String titre) {
        syllabus.addChapitre(titre);
        return this;
    }

    public Syllabus build() {
        return syllabus;
    }
}
