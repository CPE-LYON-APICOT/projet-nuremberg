package fr.cpe.engine;

// ╔══════════════════════════════════════════════════════════════════════════════╗
// ║                                                                            ║
// ║   🔒  FICHIER INTERDIT — NE PAS MODIFIER CE FICHIER  🔒                    ║
// ║                                                                            ║
// ║   Ce service gère les entrées clavier.                                     ║
// ║   Il fait partie du socle technique.                                       ║
// ║                                                                            ║
// ║   Vous pouvez l'UTILISER (lire les touches, ajouter des observers)         ║
// ║   mais ne le modifiez pas.                                                 ║
// ║                                                                            ║
// ║   Voir CONTRIBUTING.md pour savoir quels fichiers modifier.                ║
// ║                                                                            ║
// ╚══════════════════════════════════════════════════════════════════════════════╝

import com.google.inject.Singleton;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service de gestion des entrées clavier (pattern Observer).
 *
 * <h2>Deux façons de lire le clavier :</h2>
 *
 * <h3>1. Polling (dans update) — le plus simple</h3>
 * <pre>
 *   if (inputService.isKeyPressed(KeyCode.LEFT)) {
 *       // la touche gauche est enfoncée
 *   }
 * </pre>
 *
 * <h3>2. Observer (réactif) — pour des événements ponctuels</h3>
 * <pre>
 *   inputService.addKeyObserver(new KeyObserver() {
 *       public void onKeyPressed(KeyCode key) {
 *           if (key == KeyCode.SPACE) { tirer(); }
 *       }
 *       public void onKeyReleased(KeyCode key) { }
 *   });
 * </pre>
 *
 * <h2>Souris :</h2>
 * <p>Les clics souris sont gérés directement par les Nodes JavaFX :</p>
 * <pre>
 *   monCercle.setOnMouseClicked(e -&gt; {
 *       // ce cercle a été cliqué !
 *   });
 * </pre>
 *
 * <p>Ce service est un {@code @Singleton} Guice : la même instance est partagée
 * partout. Demandez-le simplement dans votre constructeur {@code @Inject}.</p>
 */
@Singleton
public class InputService {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final List<KeyObserver> keyObservers = new ArrayList<>();

    // -- Key Observer --

    public void addKeyObserver(KeyObserver observer) {
        keyObservers.add(observer);
    }

    public void removeKeyObserver(KeyObserver observer) {
        keyObservers.remove(observer);
    }

    // -- Polling --

    /**
     * Renvoie true si la touche est actuellement enfoncée.
     */
    public boolean isKeyPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    // -- Appelé par le socle (App.java) — ne pas appeler vous-même --

    public void handleKeyPressed(KeyCode key) {
        pressedKeys.add(key);
        for (KeyObserver obs : keyObservers) {
            obs.onKeyPressed(key);
        }
    }

    public void handleKeyReleased(KeyCode key) {
        pressedKeys.remove(key);
        for (KeyObserver obs : keyObservers) {
            obs.onKeyReleased(key);
        }
    }
}
