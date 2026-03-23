# CONTRIBUTING — Guide du projet

## Commandes

```bash
gradle run    # Lance le programme (fenêtre JavaFX)
gradle test   # Lance les tests unitaires
gradle build  # Compile + tests
```

---

## 🚀 Démarrer votre projet

Le code fourni contient un **exemple fonctionnel** : une balle qui rebondit et change de couleur.
Lancez `gradle run` pour le voir en action.

Quand vous êtes prêts à coder votre propre projet, voici ce que vous devez faire :

### 1. Fichiers à supprimer

Ces fichiers sont des **exemples**. Supprimez-les et créez les vôtres à la place :

| Fichier | C'est quoi | Quoi faire |
|---|---|---|
| `model/Ball.java` | Un modèle d'exemple | Supprimez-le, créez vos propres classes dans `model/` |
| `service/BallService.java` | Un service d'exemple | Supprimez-le, créez vos propres services dans `service/` |

### 2. Fichier à modifier

| Fichier | Quoi faire |
|---|---|
| `service/GameService.java` | **Videz le contenu** et mettez votre propre logique. Gardez la structure `init()` / `update()` (le moteur les appelle). |

### 3. Fichiers à NE PAS TOUCHER

| Fichier | Pourquoi |
|---|---|
| `App.java` | Démarre l'application. Si vous le cassez, plus rien ne s'affiche. |
| `AppModule.java` | Configuration technique. On y reviendra plus tard en cours. |
| `GameEngine.java` | Boucle de jeu. Appelle `init()` une fois puis `update()` ~60 fois par seconde. |
| `InputService.java` | Gestion du clavier. Vous pouvez l'**utiliser** (voir plus bas) mais pas le modifier. |
| `KeyObserver.java` | Interface Observer pour le clavier. Vous pouvez l'**implémenter** mais pas la modifier. |
| `build.gradle` | Configuration Gradle. Vous pouvez **uniquement ajouter des dépendances** dans le bloc `dependencies`. |

### En résumé

```
src/main/java/fr/cpe/
├── App.java                  🔒 NE PAS TOUCHER
├── AppModule.java            🔒 NE PAS TOUCHER (pour l'instant)
├── engine/
│   ├── GameEngine.java       🔒 NE PAS TOUCHER
│   ├── InputService.java     🔒 NE PAS TOUCHER (mais à utiliser !)
│   └── KeyObserver.java      🔒 NE PAS TOUCHER (mais à implémenter !)
├── model/
│   └── Ball.java             🗑️  EXEMPLE — à supprimer
└── service/
    ├── GameService.java      ✏️  À MODIFIER — c'est ici que vous codez
    └── BallService.java      🗑️  EXEMPLE — à supprimer
```

---

## ✏️ GameService — Le cœur de votre projet

`GameService.java` est le seul fichier que vous **devez** garder (et modifier). Le moteur l'appelle automatiquement :

1. **`init(gamePane)`** — Appelé une fois au démarrage. Créez vos éléments visuels (Nodes) ici et ajoutez-les au `gamePane`.
2. **`update(width, height)`** — Appelé ~60x/sec. Mettez à jour la logique ici (déplacements, collisions, scores…) et synchronisez les positions des Nodes.

### Comment afficher des éléments (Scene Graph)

Le projet utilise le **Scene Graph** de JavaFX : vous créez des objets visuels (Nodes)
et JavaFX les affiche automatiquement. Pas besoin de méthode `render()` !

```java
// Dans init(gamePane) :
Circle cercle = new Circle(15, Color.RED);         // Cercle de rayon 15
cercle.setCenterX(100);
cercle.setCenterY(200);

Rectangle rect = new Rectangle(50, 30, Color.BLUE); // Rectangle 50×30
rect.setX(200);
rect.setY(100);

Text texte = new Text(20, 30, "Hello !");
texte.setFill(Color.WHITE);

gamePane.getChildren().addAll(cercle, rect, texte); // Ajout au Pane
```

```java
// Dans update() — déplacer un élément :
cercle.setCenterX(cercle.getCenterX() + vitesseX);
cercle.setCenterY(cercle.getCenterY() + vitesseY);
```

### Comment détecter les clics souris

Chaque Node gère ses propres clics — pas besoin de calcul de collision :

```java
cercle.setOnMouseClicked(e -> {
    System.out.println("Le cercle a été cliqué !");
    // e.getX(), e.getY(), e.getButton()...
});
```

> Dans l'exemple fourni, cliquer sur la balle change sa couleur.

### Comment lire le clavier

Le clavier est géré par `InputService` (dans `engine/`). Deux façons de l'utiliser :

**1. Polling (dans `update`) — le plus simple :**

```java
// Ajoutez InputService en paramètre de votre constructeur @Inject
@Inject
public GameService(InputService inputService) {
    this.inputService = inputService;
}

// Puis dans update() :
if (inputService.isKeyPressed(KeyCode.SPACE)) {
    // la barre espace est enfoncée
}
```

**2. Observer (réactif) — pour des événements ponctuels :**

```java
inputService.addKeyObserver(new KeyObserver() {
    public void onKeyPressed(KeyCode key) {
        if (key == KeyCode.SPACE) { tirer(); }
    }
    public void onKeyReleased(KeyCode key) { }
});
```

> Dans l'exemple fourni, les flèches directionnelles courbent la trajectoire de la balle.

### Créer de nouveaux fichiers

Vous êtes libres de créer autant de packages et de classes que vous voulez :

```
fr.cpe.model/       → vos classes métier (entités, objets du jeu)
fr.cpe.service/     → vos services (logique, physique, IA, etc.)
```

---

## ⚙️ Note technique — Injection de dépendances

> **Vous n'avez pas besoin de comprendre cette section pour commencer.**
> On en parlera en cours. Pour l'instant, ne touchez pas aux fichiers marqués 🔒.

Le projet utilise **Google Guice**, un framework d'injection de dépendances.
C'est lui qui "câble" automatiquement les classes entre elles.

### Ce que ça fait (en résumé)

Au lieu de créer les objets à la main :

```java
// ❌ Sans injection — vous créez tout vous-même
BallService ball = new BallService();
GameService game = new GameService(ball);
```

Vous mettez `@Inject` sur le constructeur et Guice s'en occupe :

```java
// ✅ Avec Guice — il crée et injecte tout automatiquement
public class GameService {
    @Inject
    public GameService(BallService ball) {
        this.ball = ball;
    }
}
```

### Comment ça marche dans le projet

```
App.java  →  crée l'injecteur Guice
          →  demande un GameEngine
                └── Guice voit que GameEngine a besoin d'un GameService
                        └── Guice voit que GameService a besoin d'un BallService
                                └── Guice crée BallService
                        └── Guice crée GameService avec le BallService
                └── Guice crée GameEngine avec le GameService
```

Tout est automatique. Quand vous ajouterez vos propres services, il suffira de les déclarer en paramètre du constructeur avec `@Inject`.

### `AppModule.java` — Quand on en aura besoin

Quand vous utiliserez des **interfaces** (par ex. pour un Design Pattern Strategy), vous devrez dire à Guice quelle implémentation utiliser. C'est dans `AppModule` :

```java
bind(MonInterface.class).to(MonImplementation.class);
```

Tant que vous n'utilisez que des classes concrètes, Guice se débrouille tout seul — pas besoin de toucher à `AppModule`.

---

## 📦 Ajouter une dépendance externe

Dans `build.gradle`, ajoutez votre dépendance dans le bloc `dependencies` :

```groovy
dependencies {
    implementation 'com.google.inject:guice:7.0.0'       // déjà présent
    implementation 'group:artifact:version'               // ajoutez ici

    testImplementation 'org.junit.jupiter:junit-jupiter:5.11.4'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```
