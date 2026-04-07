package fr.cpe;

import fr.horizion.savoir.core.modules.formation.PDF;
import fr.horizion.savoir.models.formation.ContenuePedagogique;
import fr.horizion.savoir.models.formation.Formartion;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'exemple — remplacez par vos vrais tests.
 */
class AppTest {

//    @Test
//    void ballInitialisesCorrectly() {
//        Ball ball = new Ball(10, 20, 3, -2, Color.RED);
//        assertEquals(10, ball.x);
//        assertEquals(20, ball.y);
//        assertEquals(3, ball.dx);
//        assertEquals(-2, ball.dy);
//        assertEquals(Color.RED, ball.getColor());
//    }
//
//    @Test
//    void ballColorCanBeChanged() {
//        Ball ball = new Ball(0, 0, 0, 0, Color.RED);
//        ball.setColor(Color.BLUE);
//        assertEquals(Color.BLUE, ball.getColor());
//    }


//    @Test
    void testApp() {
        // CreatePDF
        Formartion formartion = new Formartion(1, "Formation Java", "Une formation complète sur Java", 199.99f, null, null);

        //List LesContent
        ContenuePedagogique c1 = new PDF("Introduction à Java", false, 10);
        ContenuePedagogique c2 = new PDF("Les bases de Java", false,    20);
        ContenuePedagogique c3 = new PDF("Les bases de Java", false,    30);

        List<ContenuePedagogique> contenuePedagogiques = new ArrayList<>();
        contenuePedagogiques.add(c1);
        contenuePedagogiques.add(c2);
        contenuePedagogiques.add(c3);

        formartion.ajouterContenu("pdf", "Introduction à Java", false, 10);
        System.out.println(formartion);
    }
}
