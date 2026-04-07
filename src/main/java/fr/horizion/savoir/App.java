package fr.horizion.savoir;

import fr.horizion.savoir.core.modules.formation.ContentFactory;
import fr.horizion.savoir.core.modules.payement.PaymentFactory;
import fr.horizion.savoir.core.modules.payement.PayementStrategy;
import fr.horizion.savoir.core.modules.promotion.PromotionDecorator;
import fr.horizion.savoir.models.Etudiant;
import fr.horizion.savoir.models.PaymentProcessor;
import fr.horizion.savoir.models.Syllabus;
import fr.horizion.savoir.models.formation.Formartion;
import fr.horizion.savoir.core.modules.formation.SyllabusBuilder;

import java.util.ArrayList;
import java.util.Date;

public class App {
    public static void main(String[] args) throws Exception {

        Formartion formation = new Formartion(1, "Formation Java", "Une formation complète sur Java", 199.99f,
                new ArrayList<>(), new ArrayList<>());

        formation.ajouterContenu("video", "Les bases Java", false, 45);
        formation.ajouterContenu("quiz", "Quiz 1", false, 70);
        formation.ajouterContenu("pdf", "Cours 1", false, 25);
        System.out.println("Contenus ajoutés: " + formation.getContenuePedagogiques().size() + "\n");

        Etudiant etudiant1 = new Etudiant("Dupont", "Jean", "123 rue Paris", new Date(), "jean@email.com");
        Etudiant etudiant2 = new Etudiant("Martin", "Marie", "456 rue Lyon", new Date(), "marie@email.com");

        formation.attach(etudiant1);
        formation.attach(etudiant2);
        formation.notify("Caca boudin!\n");

        PaymentFactory paymentFactory = new PaymentFactory();

        PayementStrategy stripeStrategy = paymentFactory.createPaymentStrategy("stripe");
        PaymentProcessor stripeProcessor = new PaymentProcessor(stripeStrategy);
        stripeProcessor.process(199.99f);

        PayementStrategy paypalStrategy = paymentFactory.createPaymentStrategy("paypal");
        PaymentProcessor paypalProcessor = new PaymentProcessor(paypalStrategy);
        paypalProcessor.process(199.99f);

        System.out.println();

        PromotionDecorator promotionFormation = new PromotionDecorator(formation, 20);
        System.out.println("Prix original: " + formation.getPrix() + "€");
        System.out.println("Prix avec réduction 20%: " + promotionFormation.getPrice() + "€\n");

        Syllabus syllabus = new Syllabus(1, new ArrayList<>(), new ArrayList<>());
        SyllabusBuilder builder = new SyllabusBuilder(syllabus);
        builder.addChapitre("Chapitre 1: Variables")
               .addChapitre("Chapitre 2: Boucles")
               .addChapitre("Chapitre 3: POO");

        Syllabus syllabusBuilt = builder.build();
        System.out.println("Syllabus avec " + syllabusBuilt.getChapitres().size() + " chapitres\n");

        System.out.println(formation);
    }
}

