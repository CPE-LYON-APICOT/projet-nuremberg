package fr.horizion.savoir.core.modules.formation;

import fr.horizion.savoir.models.formation.ContenuePedagogique;

public class ContentFactory {


    public ContenuePedagogique createContent(String type, String titre, Boolean estTermine, int specificVal ) {

        String tolowerType = type.toLowerCase();

        return switch (tolowerType) {
            case "video" -> new Video(titre, estTermine, specificVal);
            case "quiz" -> new Quiz(titre, estTermine, specificVal);
            case "pdf" -> new PDF(titre, estTermine, specificVal);
            default -> null;
        };
    }
}
