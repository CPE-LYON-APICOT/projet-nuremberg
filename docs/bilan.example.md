# Fiche rendu projet

> Ce document est un bilan destiné au client. Présentez ce qui a été livré, ce qui fonctionne, et tournez habilement ce qui manque. Pas de jargon technique — on parle de fonctionnalités et de valeur perçue.

## Rappel du projet

BounceWorld devait proposer une expérience interactive autour de balles rebondissantes : contrôle au clavier, personnalisation visuelle, obstacles, score et synchronisation musicale. Voici ce qui a été livré et ce qui reste à faire.

## Ce qui a été livré

### Fonctionnalité 1 — Balle rebondissante autonome

Une balle se déplace dans la fenêtre et rebondit sur les quatre bords. À chaque rebond, sa couleur change aléatoirement. Le mouvement est continu, sans traversée de bord ni ralentissement.

<!-- ![Balle rebondissante](images/bounce-demo.gif) -->

### Fonctionnalité 2 — Contrôle de trajectoire au clavier

Les flèches directionnelles courbent la trajectoire de la balle. L'accélération est progressive : la balle s'infléchit d'abord doucement, puis prend de la vitesse si on maintient la touche. On ne contrôle pas directement la balle, on influence sa direction, ce qui donne une interaction plus intéressante qu'un déplacement brut.

### Fonctionnalité 3 — Interaction au clic

Un clic sur la balle change sa couleur instantanément. C'est simple, mais le retour visuel immédiat rend le geste satisfaisant. C'est aussi la mécanique de base qui sera réutilisée pour le mode score.

### Fonctionnalité 4 — Fluidité 60 FPS

L'animation tourne à ~60 images par seconde. Le mouvement, les rebonds et la réactivité au clavier sont fluides. C'est un prérequis pour que l'expérience soit agréable, et c'est en place.

### Fonctionnalité 5 — Multiballes

L'utilisateur peut ajouter des balles d'un clic. Chacune a sa propre trajectoire, vitesse et couleur. Les balles rebondissent entre elles, ce qui crée des interactions intéressantes à partir de 4-5 balles : changements de couleur en chaîne, trajectoires imprévisibles. Au-delà de 15-20 balles, ça commence à devenir un peu chaotique (mais c'est amusant).

<!-- ![Multiballes](images/multiballs-demo.gif) -->

### Fonctionnalité 6 — Thèmes visuels

Trois thèmes disponibles : **Néon** (couleurs vives, fond sombre), **Pastel** (teintes douces, fond clair) et **Monochrome** (nuances de gris). Le changement affecte les balles et le fond. C'est cosmétique, mais ça change l'ambiance de façon notable.

<!-- ![Thèmes visuels](images/themes-demo.png) -->

## Ce qui n'a pas été livré (et pourquoi)

### Obstacles — Fonctionnel mais pas prêt

On peut placer des rectangles et les balles rebondissent dessus, mais l'interface de placement est rudimentaire (pas de glisser-déposer) et la détection de collision bugue sur les coins. On a préféré ne pas livrer une version bancale. Le mécanisme de fond est là, il manque le travail d'interface et de finition.

### Mode musique — Trop ambitieux pour le temps imparti

On a fait un prototype qui détecte les beats d'un fichier audio et synchronise les couleurs. Ça fonctionne sur du WAV, mais la gestion des formats (MP3, OGG) et des fichiers posait trop de problèmes de stabilité. C'est une feature complexe qu'on ne voulait pas livrer à moitié. Combinée au multiballes et aux thèmes, elle pourrait donner un bon résultat dans une v2.

### Score — Première version trop basique

La version initiale (clic = 1 point) fonctionnait mais n'avait pas grand intérêt — cliquer sur des grosses balles lentes, ce n'est pas un défi. On voudrait y ajouter des tailles de balles variées (petite = plus de points), des combos et un tableau des scores. Ça demande plus de travail de game design que prévu.

## Perspectives

### Court terme

- **Obstacles finalisés** : interface de placement en glisser-déposer et collisions corrigées sur les coins.
- **Gravité configurable** : un slider pour passer de l'apesanteur à une gravité forte. Facile à ajouter, change bien le comportement.
- **Traînées visuelles** : les balles laissent une trace qui s'estompe. Donne un effet visuel intéressant, surtout en mode néon.

### Moyen terme

- **Mode musique** : stabiliser le prototype, gérer plus de formats audio.
- **Score** : tailles de balles variées, combos et classement persistant.
- **Mode créatif** : l'utilisateur place obstacles et paramètres, sauvegarde sa configuration.

### Plus loin

- **Multijoueur local** : deux joueurs (flèches / ZQSD) se partagent l'écran et le score.
- **Export en GIF** : capturer une session et la partager.
- **Éditeur de thèmes** : créer ses propres palettes de couleurs.
