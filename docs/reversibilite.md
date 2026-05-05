# Document de réversibilité technique

> Ce document est destiné à l'équipe qui reprendra la maintenance du projet. Soyez honnêtes et exhaustifs. Pas d'enjolivement.

## Architecture actuelle

Le projet suit une architecture en 4 couches : **API** (serveur HTTP + routes) → **Core** (logique métier, Design Patterns) → **Models** (entités de données) → **Shared** (interfaces transversales). La persistance est assurée par MySQL via JDBC.

L'application expose deux points d'entrée :
- `App.java` : démonstration console (JavaFX prévu mais non utilisé pour l'interface actuelle).
- `ApiApplication.java` : serveur HTTP qui sert l'API REST et le frontend web statique.

```plantuml
@startuml
skinparam linetype ortho
title Architecture réelle - Horizon Savoir

package api {
    class ApiApplication
    class ApiServer
    class DatabaseConfig
    interface FormationRepository
    class MysqlFormationRepository
    record PurchaseResult
}

package "core.modules.formation" {
    class ContentFactory
    class Video
    class Quiz
    class PDF
    class SyllabusBuilder
}

package "core.modules.payement" {
    interface PayementStrategy
    class AchatManager
}

package "core.modules.payement.SubPayement" {
    class StripeStrategy
    class PayPalStrategy
    class PaymentFactory
    class PaymentResult
}

package "core.modules.promotion" {
    abstract class FormationDecorator
    class PromotionDecorator
}

package models {
    class "Formartion" as Formation
    abstract class ContenuePedagogique
    class Etudiant
    class Syllabus
    class PaymentProcessor
}

package "shared.observer" {
    interface Subject
    interface Observer
}

ApiApplication --> ApiServer
ApiServer --> FormationRepository
FormationRepository <|.. MysqlFormationRepository
MysqlFormationRepository --> DatabaseConfig
MysqlFormationRepository --> ContentFactory
MysqlFormationRepository --> AchatManager

ContentFactory ..> Video : <<create>>
ContentFactory ..> Quiz : <<create>>
ContentFactory ..> PDF : <<create>>
Video --|> ContenuePedagogique
Quiz --|> ContenuePedagogique
PDF --|> ContenuePedagogique

AchatManager --> PaymentFactory
AchatManager --> PaymentProcessor
PaymentFactory ..> StripeStrategy : <<create>>
PaymentFactory ..> PayPalStrategy : <<create>>
PayementStrategy <|.. StripeStrategy
PayementStrategy <|.. PayPalStrategy
PaymentProcessor --> PayementStrategy

Formation ..|> Subject
Etudiant ..|> Observer
Formation "1" *-- "*" ContenuePedagogique
Formation "1" o-- "1" Syllabus
SyllabusBuilder ..> Syllabus : <<build>>

FormationDecorator --|> Formation
PromotionDecorator --|> FormationDecorator
@enduml
```

**Flux d'exécution de l'API :**
1. `ApiApplication.main()` résout le port (arg → env `API_PORT` → 8080 par défaut), instancie `ApiServer` et démarre le serveur HTTP.
2. `ApiServer` enregistre deux contextes : `/` pour servir le frontend statique (HTML/CSS/JS depuis `resources/web/`), et `/api` pour les routes REST.
3. Chaque requête API passe par `handleRequest()` qui route vers les handlers par segments d'URL.
4. `MysqlFormationRepository` ouvre une connexion JDBC par requête via `DatabaseConfig.openConnection()` (pas de pool de connexions).
5. Les objets métier (`Formartion`, `Etudiant`, `ContenuePedagogique`) sont reconstruits depuis la BDD à chaque lecture.

## Bugs connus

| Bug | Sévérité | Conditions de reproduction |
|-----|----------|---------------------------|
| La classe principale du modèle s'appelle `Formartion` (faute de frappe) au lieu de `Formation`. Cela n'affecte pas le fonctionnement mais rend le code confus pour un nouveau développeur. | Mineure | Visible dans tout le code source |
| Le test `testApp()` dans `AppTest.java` n'a pas l'annotation `@Test` (commentée). Le seul test existant n'est donc jamais exécuté par `gradle test`. | Majeure | Lancer `gradle test` — aucun test ne s'exécute réellement |
| Le fichier `Draft.java` est dans le package par défaut (pas de `package`), ce qui crée un avertissement de compilation et n'est pas conforme aux conventions Java. | Mineure | Visible à la compilation |
| `PayPalStrategy.pay()` simule un paiement sans vraie intégration PayPal. Le `payPalOrderId` est toujours non-null, donc `pay()` retourne toujours `true`. | Majeure | Tout appel de paiement via PayPal réussit systématiquement, même sans configuration PayPal |
| `ContentFactory.createContent()` retourne `null` pour un type inconnu au lieu de lever une exception. Si un type invalide est passé, le contenu est silencieusement ignoré. | Mineure | Appeler `ajouterContenu("audio", ...)` — aucun contenu n'est ajouté, pas d'erreur |
| `DatabaseConfig` ouvre une nouvelle connexion JDBC à chaque requête sans pool. Sous charge, cela peut épuiser les connexions MySQL. | Majeure | Envoyer de nombreuses requêtes API simultanées |
| La clé Stripe (`STRIPE_SECRET_KEY`) est exposée en clair dans le fichier `.env` versionné. | Critique | Consulter le fichier `.env` dans le repo |

## Limitations techniques

- **Pas de pool de connexions** : chaque requête API ouvre et ferme une connexion MySQL via `DriverManager.getConnection()`. En production, il faudrait utiliser HikariCP ou un pool équivalent pour gérer les connexions efficacement.
- **Pas de tests unitaires actifs** : le seul fichier de test (`AppTest.java`) a ses méthodes commentées ou sans `@Test`. `gradle test` passe, mais ne teste rien. Il n'y a aucune couverture de test réelle.
- **Pas de validation côté API** : les requêtes POST ne valident pas la cohérence des données (prix négatif, type de contenu invalide, formation inexistante pour un achat). Seule la validation d'email est implémentée dans `Etudiant`.
- **PayPal non implémenté** : `PayPalStrategy` est un stub qui simule un paiement réussi. Les variables `clientId` et `clientSecret` sont lues mais jamais utilisées.
- **Pas d'authentification** : l'API est ouverte. N'importe qui peut créer des formations, inscrire des étudiants ou lancer des paiements sans s'identifier.
- **Pas de pagination** : `GET /api/formations` retourne toutes les formations d'un coup. Avec beaucoup de données, la réponse deviendrait volumineuse.
- **Le Decorator hérite de `Formartion`** : `FormationDecorator` étend `Formartion` au lieu de composer via une interface. Cela force à passer tous les paramètres du constructeur au parent, ce qui est fragile et peu extensible.
- **L'Observer n'est pas persisté** : les notifications sont émises en mémoire (console). À chaque redémarrage de l'API, les abonnements sont perdus car les objets `Formartion` sont recréés depuis la BDD.
- **Le `SyllabusBuilder` ne valide rien** : contrairement à la conception, le Builder ne vérifie pas la présence d'un titre ou la cohérence du syllabus avant `build()`. Il retourne simplement l'objet tel quel.
- **Interface JavaFX non utilisée** : le `build.gradle` inclut JavaFX, mais l'application réelle n'utilise pas d'interface graphique JavaFX. Le frontend est une application web (HTML/CSS/JS) servie par le serveur HTTP embarqué.

## Points de vigilance pour la reprise

- **Typo `Formartion`** : le nom de la classe est `Formartion` partout (modèle, repository, API, tests). Un renommage global est nécessaire mais affectera tous les fichiers du projet. Utiliser le refactoring automatique de l'IDE.
- **Typo `PayementStrategy`** : l'interface s'appelle `PayementStrategy` au lieu de `PaymentStrategy`. Même remarque que ci-dessus.
- **Package `SubPayement`** : le sous-package s'appelle `SubPayement` — nom peu conventionnel. Les classes `StripeStrategy`, `PayPalStrategy`, `PaymentFactory` et `PaymentResult` y sont regroupées.
- **`PaymentProcessor` est dans `models/`** : cette classe contient de la logique métier (traitement de paiement) mais se trouve dans le package `models`. Elle devrait être dans `core.modules.payement`.
- **Pas de fichier `.env.example`** : les variables d'environnement attendues (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `STRIPE_SECRET_KEY`, `API_PORT`) ne sont documentées nulle part hormis dans le code et le `docker-compose.yml`.
- **`Draft.java` à supprimer** : fichier de brouillon laissé dans le code source, contient du code de test manuel. Il ne devrait pas être présent dans un livrable.
- **La sérialisation JSON des modèles** : `ObjectMapper` de Jackson sérialise directement les objets Java. Ajouter une propriété à `Formartion` ou `Etudiant` l'exposera automatiquement dans l'API. Pensez à utiliser des DTOs si vous voulez contrôler la réponse.
- **MySQL requis** : l'API ne démarre pas sans MySQL. Il n'y a pas de mode « en mémoire » pour le développement sans Docker. Lancer `docker compose up` est un prérequis.
- **Les scripts SQL** (`schema.sql`, `data.sql`) utilisent `GENERATED ALWAYS AS IDENTITY` qui est du SQL standard mais pas toujours supporté par MySQL 8.0 dans toutes les configurations. Le `docker-compose.yml` initialise la BDD via `docker/mysql/init/`.

## Améliorations recommandées

| Amélioration | Difficulté | Justification |
|--------------|------------|---------------|
| Renommer `Formartion` → `Formation` et `PayementStrategy` → `PaymentStrategy` | Facile | Corrige les fautes de frappe, améliore la lisibilité pour tout nouveau développeur |
| Ajouter un pool de connexions (HikariCP) | Facile | Évite l'épuisement des connexions MySQL sous charge, améliore les performances |
| Écrire de vrais tests unitaires avec `@Test` | Moyen | Aucun test ne s'exécute actuellement — la CI passe à vide. Couvrir au minimum `ContentFactory`, `AchatManager`, `PromotionDecorator` et `SyllabusBuilder` |
| Implémenter une vraie intégration PayPal | Moyen | `PayPalStrategy` est un stub. Utiliser le SDK PayPal Java pour de vrais paiements |
| Ajouter de l'authentification sur l'API (JWT ou sessions) | Moyen | L'API est entièrement ouverte, n'importe qui peut modifier les données |
| Extraire les DTO de réponse API | Facile | Évite d'exposer directement les objets métier dans les réponses JSON |
| Ajouter de la validation sur les entrées API | Facile | Les requêtes invalides ne sont pas rejetées proprement (prix négatifs, types inconnus) |
| Supprimer `Draft.java` et nettoyer le code mort | Facile | Fichier de brouillon qui ne devrait pas être dans le livrable |
| Faire que `FormationDecorator` compose via une interface au lieu d'hériter de `Formartion` | Moyen | Architecture Decorator plus propre et moins couplée |
| Persister les notifications en BDD | Moyen | La table `notifications` existe dans le schéma SQL mais n'est pas alimentée par le code |
| Ajouter la pagination sur `GET /api/formations` | Facile | Indispensable si le nombre de formations augmente |
| Retirer la clé Stripe du `.env` versionné et utiliser des secrets | Facile | Faille de sécurité critique : la clé API est visible dans l'historique Git |
| Ajouter de la validation dans `SyllabusBuilder.build()` | Facile | Le Builder ne vérifie rien avant de construire — ajouter au minimum un check sur la présence de chapitres |
