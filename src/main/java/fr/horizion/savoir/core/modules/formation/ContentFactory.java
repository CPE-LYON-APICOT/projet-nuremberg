package fr.horizion.savoir.core.modules.formation;

import fr.horizion.savoir.models.formation.ContenuePedagogique;

public class ContentFactory {


    public ContenuePedagogique createContent(String type, String titre, Boolean estTermine, int specificVal ) {

        return switch (type) {
            case "video" -> {
                yield new Video(titre, estTermine, specificVal);


            }

            case "quiz" -> {
                yield new Quiz( titre, estTermine, specificVal);
            }

            case "pdf" -> {
                yield new PDF( titre, estTermine, specificVal);
            }
            default -> null;
        };
    }
}
