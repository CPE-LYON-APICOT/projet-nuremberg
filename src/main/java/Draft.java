import fr.horizion.savoir.core.modules.formation.PDF;
import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.models.formation.ContenuePedagogique;
import fr.horizion.savoir.models.formation.Formartion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Draft {

    public static void main(String[] args) {
        //List LesContent
        ContenuePedagogique c1 = new PDF("Introduction à Java", false, 10);
        ContenuePedagogique c2 = new PDF("Les bases de Java", false,    20);
        ContenuePedagogique c3 = new PDF("Les bases de Java", false,    30);

        Etudiant e1 = new Etudiant("SQDSQ", "SDQSDSQ", "sdfsqd", new Date(), "dqdsqds@gmail.com");

        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(e1);
        List<ContenuePedagogique> contenuePedagogiques = new ArrayList<>();
        contenuePedagogiques.add(c1);
        contenuePedagogiques.add(c2);
        contenuePedagogiques.add(c3);


        Formartion formartion = new Formartion(1, "Formation Java", "Une formation complète sur Java", 199.99f,etudiants , contenuePedagogiques);



        for (ContenuePedagogique contenuePedagogique : contenuePedagogiques) {
            System.out.println(contenuePedagogique.affiche());
        }

        formartion.ajouterContenu("pdf", "Introduction à Java", false, 10);
        System.out.println(formartion);
    }
}
