# Conception technique

> Ce document décrit l'architecture technique de votre projet. Vous êtes dans le rôle du lead-dev / architecte. C'est un document technique destiné à des développeurs.

## Vue d'ensemble

<!-- Décrivez les grandes briques de votre application et comment elles communiquent. Un schéma d'architecture est bienvenu. -->

## Design Patterns

### DP 1 — *Nom du pattern*

**Feature associée** : 

**Justification** : 
<!-- Pourquoi ce pattern ? Pourquoi pas un autre ? -->

**Intégration** : 
<!-- Comment s'intègre-t-il dans l'architecture ? -->

### DP 2 — *Nom du pattern*

**Feature associée** : 

**Justification** : 

**Intégration** : 

### DP 3 — *Nom du pattern*

**Feature associée** : 

**Justification** : 

**Intégration** : 

### DP 4 — *Nom du pattern*

**Feature associée** : 

**Justification** : 

**Intégration** : 

## Diagrammes UML

### Diagramme 1 — *Type (classe, séquence, cas d'utilisation…)*

<!-- Exemple de syntaxe PlantUML (à remplacer par votre diagramme) :

```plantuml
@startuml
interface Drawable {
    + draw(gc : GraphicsContext) : void
}

abstract class Entity {
    - x : double
    - y : double
    + getX() : double
    + getY() : double
    + update() : void
}

Entity ..|> Drawable

class Player extends Entity {
    - speed : double
    + move(direction : Direction) : void
}

class Obstacle extends Entity {
    - damage : int
}
@enduml
```

Ceci est un exemple, remplacez-le par votre propre diagramme. -->

```plantuml
@startuml

@enduml
```

### Diagramme 2 — *Type*

```plantuml
@startuml

@enduml
```

