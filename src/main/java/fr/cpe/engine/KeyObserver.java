package fr.cpe.engine;

import javafx.scene.input.KeyCode;

/**
 * Interface Observer pour les événements clavier.
 *
 * <p>Implémentez cette interface et enregistrez-vous auprès de
 * {@link InputService#addObserver(KeyObserver)} pour être notifié
 * des appuis et relâchements de touches.</p>
 *
 * <p>Exemple :</p>
 * <pre>
 *   inputService.addObserver(new KeyObserver() {
 *       public void onKeyPressed(KeyCode key) {
 *           if (key == KeyCode.SPACE) { tirer(); }
 *       }
 *       public void onKeyReleased(KeyCode key) { }
 *   });
 * </pre>
 */
public interface KeyObserver {

    void onKeyPressed(KeyCode key);

    void onKeyReleased(KeyCode key);
}
