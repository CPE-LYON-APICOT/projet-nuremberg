# Conception technique

> Ce document décrit l'architecture technique de votre projet. Vous êtes dans le rôle du lead-dev / architecte. C'est un document technique destiné à des développeurs.

## Vue d'ensemble

<!-- Décrivez les grandes briques de votre application et comment elles communiquent. Un schéma d'architecture est bienvenu. -->

## Design Patterns

### DP 1 — Factory Method

**Feature associée** : Fonctionnalité 1 — Suivi de progression intelligent.

**Justification** : Centralise la création des différents supports (Vidéo, Quiz, PDF). Cela permet au système de suivi de manipuler une interface commune sans connaître les détails techniques de chaque média.

**Intégration** : Une `ContentFactory` instancie le bon module pédagogique selon les données de la base.

### DP 2 — Strategy & Factory

**Feature associée** : Fonctionnalité 2 — Paiement sécurisé et instantané.

**Justification** : Le **Strategy** rend l algorithme de paiement interchangeable (Stripe, PayPal). On y ajoute une **Factory** pour instancier la bonne stratégie selon le choix du client, isolant totalement la logique financière.

**Intégration** : Le `PaymentProcessor` demande une stratégie à la `PaymentFactory` et exécute le paiement de manière générique.

### DP 3 — Observer

**Feature associée** : Fonctionnalité 3 — Système d alertes automatiques.

**Justification** : Évite un couplage fort entre les cours et les notifications. Le cours se contente d émettre un signal de mise à jour, et les abonnés réagissent selon leurs préférences (Email, Push).

**Intégration** : La classe `Formation` (Sujet) notifie ses `Observers` (Étudiants) dès qu un nouveau contenu est validé.

### DP 4 — Decorator

**Feature associée** : Fonctionnalité 4 — Offres et options personnalisées.

**Justification** : Permet d ajouter des couches de prix ou de services (Promotions, Certificats) à une formation de base sans multiplier les sous-classes inutiles. C est la solution la plus flexible pour le calcul de prix dynamique.

**Intégration** : Un `PromotionDecorator` enveloppe l objet `Formation` pour appliquer une réduction sur la méthode `getPrice()`.

### DP 5 — Builder

**Feature associée** : Fonctionnalité 5 — Organisateur de cours structuré.

**Justification** : La création d un syllabus est complexe et sujette aux erreurs de saisie. Le Builder permet de construire le plan de cours étape par étape et de valider la cohérence (ex: présence d un titre) avant de créer l objet final.

**Intégration** : La classe interne `SyllabusBuilder` rassemble les éléments du cours et verrouille l objet une fois construit pour garantir l intégrité des données.


## Diagrammes UML

### Diagramme 1 — *Type (classe, séquence, cas d'utilisation…)*


![IMGDiag1](https://cdnvoid.jessim.ovh/items/diag1Class.png)
```plantuml
@startuml
skinparam style underline
title Diagramme de Classe - Horizon Savoir

' --- INTERFACES ---
interface Subject {
    +attach(observer: Observer)
    +detach(observer: Observer)
    +notify()
}

interface Observer {
    +update(message: String)
}

interface PaymentStrategy {
    +pay(amount: float): boolean
}

' --- CLASSES ---
class Formation {
    -id: int
    -titre: String
    -description: String
    -prixBase: float
    -listeEleves: List<Observer>
    +getPrice(): float
    +ajouterContenu(c: ContenuPedagogique)
}

class Etudiant {
    -nom: String
    -email: String
    +update(message: String)
}

abstract class ContenuPedagogique {
    -titre: String
    -estTermine: boolean
    {abstract} +afficher()
}

class Video {
    -duree: int
}

class Quiz {
    -scoreMin: int
}

class PDF {
    -nbPages: int
}

class ContentFactory {
    +createContent(type: String): ContenuPedagogique
}

class StripeStrategy {
    +pay(amount: float): boolean
}

class PayPalStrategy {
    +pay(amount: float): boolean
}

class PaymentProcessor {
    -strategy: PaymentStrategy
    +process(amount: float)
}

abstract class FormationDecorator {
    #wrappedFormation: Formation
    +getPrice(): float
}

class PromotionDecorator {
    -remise: float
    +getPrice(): float
}

class Syllabus {
    -chapitres: List<String>
    -objectifs: List<String>
}

class SyllabusBuilder {
    -syllabus: Syllabus
    +addChapitre(titre: String): SyllabusBuilder
    +build(): Syllabus
}

' --- RELATIONS ---
Subject <|.. Formation
Observer <|.. Etudiant
ContenuPedagogique <|-- Video
ContenuPedagogique <|-- Quiz
ContenuPedagogique <|-- PDF
PaymentStrategy <|.. StripeStrategy
PaymentStrategy <|.. PayPalStrategy
Formation <|-- FormationDecorator
FormationDecorator <|-- PromotionDecorator

Formation "1" *-- "*" ContenuPedagogique
Formation "1" o-- "1" Syllabus
ContentFactory ..> ContenuPedagogique : <<create>>
PaymentProcessor --> PaymentStrategy
SyllabusBuilder ..> Syllabus : <<build>>

@enduml
```


### Diagramme 2 — *Type*

![IMGdiag2](https://cdnvoid.jessim.ovh/items/sequenceForm.png)
```plantuml
@startuml
title Scénario : Achat d'une formation et Notification

actor Client_Final
participant "AchatManager" as AM
participant "PaymentFactory" as PF
participant "StripeStrategy" as Stripe
participant "Formation (Subject)" as F
participant "ContentFactory" as CF
participant "Client_Final (Observer)" as Obs

Client_Final -> AM : acheterFormation(id)
activate AM

' --- STRATEGY PATTERN ---
AM -> PF : getPaymentMethod("Stripe")
activate PF
PF --> AM : strategyInstance
deactivate PF

AM -> Stripe : pay(montant)
activate Stripe
Stripe --> AM : success
deactivate Stripe

' --- LOGIQUE MÉTIER ---
AM -> F : ajouterEleve(Client_Final)
activate F

' --- OBSERVER PATTERN ---
F -> F : notify()
F -> Obs : update("Bienvenue dans la formation !")
activate Obs
Obs --> F : ack
deactivate Obs

' --- FACTORY METHOD ---
F -> CF : createContent("VideoBienvenue")
activate CF
CF --> F : instanceVideo
deactivate CF

F --> AM : confirmation
deactivate F

AM --> Client_Final : Affichage du succès
deactivate AM

@enduml
```

