# Fiche rendu projet

> Ce document est un bilan destiné au client. Présentez ce qui a été livré, ce qui fonctionne, et tournez habilement ce qui manque. Pas de jargon technique — on parle de fonctionnalités et de valeur perçue.

## Rappel du projet

Horizon Savoir est une plateforme web de formation en ligne. L'objectif initial était de proposer un espace simple et intuitif où les formateurs créent leurs cours et les élèves les suivent à leur rythme. Nous avions promis cinq fonctionnalités clés : un suivi de progression intelligent, un paiement sécurisé et instantané, un système d'alertes automatiques, des offres et options personnalisées, et un organisateur de cours structuré. L'application devait être accessible via une interface web moderne, connectée à une base de données et prête à être déployée.

## Ce qui a été livré

### Fonctionnalité 1 — Suivi de progression intelligent

Chaque formation contient des modules pédagogiques (vidéos, quiz, documents PDF) et le système calcule automatiquement le taux de progression de l'élève en fonction des contenus qu'il a terminés. L'élève peut reprendre son parcours exactement là où il s'était arrêté. Via l'interface web, un formateur peut marquer un module comme terminé et voir instantanément la progression mise à jour sous forme de pourcentage.

<!-- ![Suivi de progression](images/progression-demo.png) -->

### Fonctionnalité 2 — Paiement sécurisé et instantané

Nous avons intégré deux solutions de paiement reconnues : **Stripe** et **PayPal**. Lorsqu'un élève achète une formation, le système traite le paiement en temps réel via le prestataire choisi, enregistre la transaction en base de données, puis inscrit automatiquement l'élève à la formation. Le tout se fait en une seule action depuis l'interface web : l'élève remplit ses informations, choisit son moyen de paiement, et son accès est débloqué immédiatement après validation.

<!-- ![Formulaire d'achat](images/achat-demo.png) -->

### Fonctionnalité 3 — Système d'alertes automatiques

La plateforme notifie automatiquement les élèves inscrits à une formation dès qu'un événement important survient : ajout d'un nouveau contenu pédagogique, validation d'un module, ou inscription confirmée. Chaque élève reçoit un message personnalisé (par exemple : « Bienvenue dans la formation : Formation Java » ou « Nouveau contenu ajouté : Quiz 1 »). Ce mécanisme de notification fonctionne de manière transparente et garantit que personne ne manque une mise à jour.

### Fonctionnalité 4 — Offres et options personnalisées

Les formateurs peuvent créer des promotions sur leurs formations. Le système applique des réductions en pourcentage sur le prix de base, et ces réductions sont cumulables : il est possible d'empiler plusieurs offres (par exemple, une remise « nouveaux inscrits » puis une remise « fin de mois ») sans modifier le prix original de la formation. Cela permet une grande souplesse commerciale, et le prix affiché au client est toujours le prix final après réduction.

### Fonctionnalité 5 — Organisateur de cours structuré

Les formateurs disposent d'un outil pour construire le plan de cours (syllabus) de leurs formations étape par étape : ajout de chapitres, définition d'objectifs pédagogiques, dans un ordre logique. Le syllabus est construit progressivement et verrouillé une fois validé, ce qui garantit l'intégrité du programme. Cela offre aux élèves une vision claire du parcours avant même de s'inscrire.

### Bonus — Interface web et API complète

En complément des fonctionnalités métier, nous avons livré :

- **Une interface web moderne** accessible depuis n'importe quel navigateur, avec un tableau de bord qui affiche en temps réel l'état du serveur, la liste des formations, et les statistiques de progression.
- **Une API REST complète** permettant la gestion de toutes les opérations : créer une formation, inscrire un étudiant, ajouter du contenu, suivre la progression, et effectuer un achat. Cette API est prête à être connectée à une application mobile ou à un outil tiers.
- **Un déploiement simplifié** grâce à Docker : la base de données MySQL, l'interface d'administration phpMyAdmin et l'API sont lancées en une seule commande (`docker compose up`).

## Ce qui n'a pas été livré (et pourquoi)

### Notifications par email / push — Prêt à évoluer

Le système d'alertes fonctionne en temps réel au sein de la plateforme, mais les notifications ne sont pas encore envoyées par email ou notification push sur mobile. Le mécanisme est en place : dès qu'un événement survient, le message est généré et transmis aux abonnés. Il suffit d'ajouter un canal de diffusion (email via un service comme SendGrid, ou push via Firebase) pour étendre la portée des alertes. C'est une évolution rapide et à fort impact pour l'engagement des élèves.

### Espace communautaire — Prévu pour la V2

Nous avions mentionné un espace de discussion entre élèves dans les perspectives du pitch. Cette fonctionnalité nécessite un module de messagerie temps réel et une modération des échanges, ce qui représente un chantier à part entière. La base de données est déjà structurée pour accueillir des interactions entre utilisateurs, ce qui facilitera grandement l'intégration dans une prochaine version.

### Mode hors-ligne — Une opportunité premium

La consultation des leçons sans connexion internet fait partie des évolutions souhaitées. Techniquement, cela implique un système de cache local côté client et une synchronisation différée avec le serveur. Combiné au suivi de progression existant, le mode hors-ligne permettrait de proposer une offre « premium » aux utilisateurs nomades — une vraie différenciation commerciale.

## Perspectives

### Court terme

- **Notifications par email** : brancher un service d'envoi d'emails sur le système d'alertes existant pour informer les élèves même lorsqu'ils ne sont pas sur la plateforme.
- **Tableau de bord formateur** : offrir aux formateurs une vue détaillée sur les statistiques d'inscription, les revenus générés et la progression moyenne de leurs élèves.
- **Certificats de réussite** : générer automatiquement un certificat personnalisé lorsqu'un élève atteint 100 % de progression sur une formation.

### Moyen terme

- **Espace communautaire** : ajouter un forum de discussion par formation pour favoriser l'entraide entre élèves et l'engagement sur la plateforme.
- **Application mobile** : proposer une application dédiée pour suivre ses formations en déplacement, avec des notifications push et un accès hors-ligne aux contenus déjà téléchargés.
- **Système d'avis** : permettre aux élèves de noter et commenter les formations pour guider les futurs inscrits et aider les formateurs à améliorer leur contenu.

### Plus loin

- **Intelligence artificielle** : recommander des formations adaptées au profil et à la progression de chaque élève, pour un parcours d'apprentissage véritablement personnalisé.
- **Intégration LMS** : connecter Horizon Savoir aux outils existants des entreprises et écoles (Moodle, Teams) pour faciliter l'adoption dans un contexte professionnel.
- **Internationalisation** : proposer la plateforme en plusieurs langues pour toucher un public international.
