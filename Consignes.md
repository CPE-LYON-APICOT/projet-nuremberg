# Projet POO — 3ICS

Projet en binôme. Dernier rendu de TP du module POO.

Ce projet évalue vos compétences en programmation orientée objet **et** vos compétences professionnelles : relation client, documentation, conception et réversibilité.

Concrètement, vous allez :
- **Imaginer** un projet logiciel et le vendre à un client fictif (le pitch).
- **Concevoir** l'architecture technique avant de coder (la conception).
- **Implémenter** le projet en Java avec des Design Patterns.
- **Faire le bilan** de ce qui a été livré (la fiche rendu).
- **Documenter la passation** pour une équipe de maintenance (la réversibilité).

Ces étapes reflètent un cycle de projet réel en entreprise. Chacune a un poids dans la notation.

### Prise en main

Le repo que vous avez cloné contient un projet Gradle prêt à l'emploi. Vous n'avez pas à configurer Gradle, il est déjà fonctionnel. Vérifiez-le immédiatement :

```bash
gradle run   # Lance le programme
gradle test  # Lance les tests
```

> **Si ces commandes ne fonctionnent pas dès le départ, signalez-le immédiatement.**

---

## Exigences

### Techniques

| Exigence | Détail |
|---|---|
| **Langage** | Java uniquement |
| **Build** | Gradle (fourni, ne pas modifier la config sauf ajout de dépendances) |
| **Versioning** | Git + GitHub Classroom |
| **Interface graphique** | JavaFX avec Canvas (le socle graphique vous est fourni, vous dessinez la partie métier) |
| **Design Patterns** | Au minimum **4 DP** parmi : **Singleton, Strategy, Observer, Factory, Decorator**. Vous pouvez en utiliser d'autres si vous les maîtrisez, mais les 4 obligatoires doivent venir de cette liste. |
| **UML** | Au minimum **2 diagrammes UML** dans votre conception technique (classe, séquence, cas d'utilisation…) |
| **Documentation** | Tous les documents sont rédigés en **Markdown** dans le dossier `docs/` |
| **Tests** | Vos tests doivent passer via `gradle test` au moment du rendu |
| **Cours** | Vous mettez en application chaque élément du cours au moins une fois |

#### Précisions sur les Design Patterns

Chaque DP doit être **justifié par une feature**. On ne colle pas un Singleton "parce qu'il en faut un" — on l'utilise parce qu'une contrainte du projet l'exige (par exemple, un gestionnaire de configuration unique).

Dans votre conception technique, vous devez expliquer pour chaque DP :
- Quelle feature il sert.
- Pourquoi ce pattern est adapté (et pas un autre).
- Comment il s'intègre dans votre architecture (diagramme UML bienvenu).

#### Précisions sur l'interface graphique

Le socle JavaFX/Canvas vous est fourni : vous n'avez pas à gérer la fenêtre, la boucle de rendu ou les événements de base. Votre travail consiste à **dessiner votre logique métier** sur le Canvas. Pensez-y comme une toile blanche : le chevalet est monté, à vous de peindre.

### Bibliothèques externes

Vous êtes libres d'utiliser des bibliothèques tierces (moteur physique, lib audio, etc.). En revanche, **aucun support ne sera fourni** dessus. Si vous bloquez sur une lib, vous vous débrouillez.

Ajoutez vos dépendances dans le `build.gradle` fourni. Si vous ne savez pas comment, demandez — c'est rapide à montrer.

### PlantUML

Vous pouvez intégrer des diagrammes PlantUML directement dans vos fichiers Markdown.

[Liste des diagrammes supportés](https://plantuml.com/fr/sitemap-language-specification)

---

## Livrables

Vous avez **5 livrables** à produire. Chacun a un objectif pédagogique distinct.

### 1. Pitch du projet

📄 `docs/pitch.md`

Vous êtes en train de présenter votre projet à un client potentiel. Ce client **n'est pas informaticien** : il ne sait pas ce qu'est un objet, une classe ou un Design Pattern. En revanche, il comprend les diagrammes et les explications simples.

**Objectif** : vendre votre projet. On parle de fonctionnalités, de valeur ajoutée, de potentiel. Pas de code.

En rédigeant votre pitch, vous allez naturellement identifier les opportunités et les défis de votre projet — et donc anticiper les Design Patterns à utiliser. C'est voulu : la réflexion métier précède toujours la réflexion technique.

**Soyez ambitieux.** Vous n'arriverez probablement pas à tout faire, c'est prévu et c'est pédagogique. Un pitch trop modeste limitera vos possibilités de DP (et donc votre note).

Le pitch doit être validé **avant** de commencer à coder. Committez-le dans la PR "Feedback" pour validation. Tant qu'il n'est pas validé, ne codez pas.

> **Ce livrable développe votre fibre relation client. Soignez la présentation : structurez, illustrez, donnez envie.**

> IA : L'usage est autorisé pour la rédaction du pitch, conditionnez la en mode "Commercial" pour qu'elle vende du rêve au client, attention, elle risque de vendre monts et merveilles alors que vous n'arriverez pas à tout réaliser (comme en entreprise 🙃)



### 2. Conception technique

📄 `docs/conception.md`

Suite à la validation du pitch, je vous donnerai des pistes : "Ajoute cette feature, ça te permettra d'utiliser tel DP."

Vous rédigez alors une annexe technique qui décrit **comment** vous allez réaliser le projet :

- Quels DP vous allez utiliser et quelle feature chacun permet de réaliser.
- Au moins 2 diagrammes UML.

C'est un document technique. Vous êtes dans le rôle du lead-dev / architecte. Je vous aiderai sur cette partie car c'est un exercice complexe et ce n'est pas encore votre niveau de formation.

La conception doit être validée **avant** de coder. Comme le pitch, tant qu'elle n'est pas validée, ne codez pas.

> Prenez du recul pour identifier comment implémenter les fonctionnalités de manière robuste et évolutive.  
> C'est un exercice de planification et d'anticipation, mais c'est probablement la partie la plus importante du projet. Un code sans conception est voué à l'échec, tandis qu'une bonne conception vous guidera tout au long du développement.

**Ce qu'on attend concrètement :**
- Pour chaque DP : le nom du pattern, la feature associée, un paragraphe de justification, et idéalement un diagramme.
- Au moins 2 diagrammes UML au total (pas forcément un par DP). Choisissez les types de diagrammes les plus pertinents pour votre projet.
- Une vue d'ensemble de l'architecture : quelles sont les grandes briques de votre application et comment elles communiquent.

### 3. Fiche rendu projet ⚠️ *À rendre en fin de projet*

📄 `docs/bilan.md`

Vous avez vendu un projet, vous l'avez réalisé, il est temps de faire le bilan **au client**.

Présentez ce qui a fonctionné. Montrez des captures d'écran (vous pouvez utiliser ScreenToGif ou équivalent pour des captures animées). Si des features manquent, expliquez pourquoi soyez malin et honnête.

> Vous pouvez présenter la vérité qui vous arrange, on ne parle pas de technique dans ce document, mais de fonctionnalités / valeur perçue.   
>
> Par exemple, vous aviez promis que votre jeu de plateforme aurait une génération procédurale de niveaux, mais vous n'avez pas eu le temps de l'implémenter. Vous pouvez présenter une version "démo" avec des niveaux préconçus, et expliquer que la génération procédurale est une amélioration future qui permettra d'avoir des niveaux infinis.  
>
> En revanche, peut-être auriez-vous le temps rapidement d'implémenter un système de skins ou d'ajouter des effets sonores, qui sont assez simples à faire (pour vous) mais qui laissent une meilleure impression au client que de présenter une démo incomplète.   
> Si vous êtes encore plus malin, présentez ce qu'il manque comme une opportunité de DLC avec la partie qu'il vous manque + une évolution qui succite l'engouement    
> Exemple : *"Pour revenir à la génération procédurale, maintenant que nous avons des skins/thèmes, ceux-ci pourraient influencer la génération de niveau pour davantage d'immersion, ce qui serait un excellent sujet de DLC."*


Même si vous ne devez absolument pas parler de technique dans ce document, il faut quand même que vos 4 *features* qui s'appuient sur 4 DP soient présentes.  

### 4. Document de réversibilité technique ⚠️ *À rendre en fin de projet*

📄 `docs/reversibilite.md`

Le projet est livré, la maintenance est confiée à une autre équipe. En entreprise, la réversibilité technique est une prestation due au client.

Ici, **pas d'enjolivement**. Soyez honnêtes et exhaustifs :

- **Bugs connus** — Listez-les, même les mineurs. Précisez les conditions de reproduction si possible.
- **Limitations techniques** — Ce qui ne fonctionne pas ou fonctionne partiellement. Par exemple : "Le jeu lag au-delà de 50 entités affichées."
- **Points de vigilance pour la reprise** — Ce qu'un développeur reprenant le projet doit absolument savoir. Par exemple : "La classe `GameEngine` est devenue un God Object, il faudrait la refactoriser."
- **Améliorations recommandées** — Ce que vous feriez si vous aviez plus de temps, avec une estimation de difficulté (facile / moyen / complexe).
- **Architecture actuelle** — Un diagramme de classes ou de composants reflétant l'état réel du code (pas la conception initiale, l'état final).

Ce document est le plus technique des cinq. C'est ici que vous montrez votre lucidité d'ingénieur.

### 5. Le code

Le projet Gradle est fourni. Vous n'avez qu'à écrire votre code métier et vos tests.

- `gradle run` doit lancer votre programme.
- `gradle test` doit exécuter vos tests et ils doivent passer.

**Bonnes pratiques attendues :**
- Des commits réguliers et descriptifs (pas de `fix`, `update`, `wip` comme seul message).
- Une organisation logique des packages.
- Du code lisible : noms de variables/méthodes/classes explicites, pas de méthodes de 200 lignes.
- Des tests unitaires qui testent réellement quelque chose (pas juste `assertTrue(true)`).
- Pas de code mort ou commenté sans raison.

---

## Repo Git et PR Feedback

Votre repo GitHub Classroom a une **PR ouverte "Feedback"**. Chaque push enrichit cette PR. La commande `gradle test` est exécutée automatiquement et les résultats y sont affichés.

Utilisez cette PR comme canal de communication : je validerai vos livrables et laisserai des commentaires directement dedans.

**Comment ça marche concrètement :**
1. Vous codez / rédigez en local.
2. Vous committez et poussez sur `main` (ou une branche que vous mergez dans `main`).
3. La PR "Feedback" se met à jour automatiquement.
4. Je lis, je commente, vous corrigez.

> Pensez à consulter régulièrement la PR pour voir si j'ai laissé des retours.

---

## Déroulement du projet

Le projet se déroule sur environ **6 séances** jusqu'à la fin du module. On adopte une approche itérative.

### Étape par étape

1. **Enrôlez-vous** sur GitHub Classroom : 
**XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX**
1. **Rédigez votre pitch** — ne codez pas encore. C'est la première chose à faire.
1. **Faites valider le pitch** en le committant dans la PR. Je valide en séance ou via commentaires.
1. **Rédigez votre conception technique** et faites-la valider. On en discutera ensemble.
1. **Codez itérativement**. À chaque itération, faites une démo. Le "client" (moi) peut changer d'avis, comme en entreprise.  
Si votre code est fragile ou incompris, je vous demanderai de refactoriser.
1. **Rendez les documents de fin** (fiche rendu + réversibilité).

N'hésitez pas à utiliser des branches Git pour organiser vos itérations.

### L'approche itérative, concrètement

Ne cherchez pas à tout coder d'un coup. À chaque séance, visez un **incrément fonctionnel** :

- **Séance 1** : Pitch rédigé et soumis.
- **Séance 2** : Conception technique validée, premières briques de code.
- **Séances 3-5** : Implémentation itérative, démos régulières, intégration des retours.
- **Séance 6** : Finalisation, rédaction des documents de fin, nettoyage du code et des tests.

Ce découpage est indicatif. L'important est d'**avoir quelque chose qui fonctionne à chaque étape**, même si c'est partiel. Un projet qui affiche un écran avec un carré qui bouge vaut mieux qu'un projet ambitieux qui ne compile pas.

---

## Évaluation

Il n'y a pas de barème chiffré. Les critères de notation sont :

- **La qualité de vos documents** — c'est au moins aussi important que le code. Un pitch bâclé, une conception vide ou une réversibilité de deux lignes seront sévèrement sanctionnés. À l'inverse, des documents soignés, structurés et réfléchis peuvent rattraper un code incomplet.
- **La qualité de votre code** — propreté, respect des principes POO, utilisation pertinente des DP, tests significatifs.
- **Votre démarche** — comment vous avez abordé le projet, vos itérations, votre capacité à vous adapter aux retours. Je regarde l'historique Git : des commits réguliers et bien nommés valent mieux qu'un gros push la veille du rendu.
- **L'évolution de votre projet** — visible à travers l'historique Git, les commits, les démos successives. Je veux voir une progression, pas un produit fini sorti de nulle part.

> Une implémentation parfaite avec des documents vides ne suffit pas.  
> Des documents parfaits avec un code qui ne compile pas non plus.  
> L'équilibre entre les deux, avec une démarche visible et cohérente, c'est ça qui fait la différence.

---

## Utilisation de l'IA

L'utilisation d'outils IA (Copilot, ChatGPT…) **n'est pas interdite**. Un fichier `.github/copilot-instructions.md` est fourni dans le repo : il configure GitHub Copilot pour qu'il se comporte comme un **tuteur** et non comme un assistant de code. Ce fichier est lu automatiquement par Copilot dans **VS Code et IntelliJ**. **Ne modifiez pas ce fichier.**

> 📖 Un guide de mise en place de GitHub Copilot est disponible dans [docs/setup-copilot.md](docs/setup-copilot.md).

### Règles

- Si l'IA génère du code que vous ne comprenez pas, **demandez-moi de l'aide**. Je préfère passer 5 minutes à vous expliquer 10 lignes que 30 minutes à débugger 100 lignes que vous n'avez pas écrites.
- Si je détecte du code IA que vous ne maîtrisez pas, je vous demanderai de changer la feature et de recommencer. Vous perdrez du temps et cela se verra dans votre progression.

### Bonus / Malus

Une **bonne utilisation de l'IA** sera valorisée :
- Lui demander d'expliquer un concept ou un pattern.
- Lui soumettre une erreur de compilation pour comprendre le problème.
- Explorer une piste d'architecture avec elle, puis l'implémenter vous-même.
- Lui faire relire votre code pour identifier des améliorations.

Une **mauvaise utilisation** sera pénalisée :
- Génération massive de code que vous ne comprenez pas.
- Copier-coller sans réflexion.
- Laisser l'IA écrire vos documents à votre place (ça se voit).
- Désactiver ou contourner le fichier `.github/copilot-instructions.md`.


---

## Structure attendue du repo

```
.
├── .github/
│   ├── copilot-instructions.md # Configuration IA (fourni, ne pas modifier)
│   └── workflows/test.yml      # CI automatique
├── docs/
│   ├── images/                 # Captures d'écran
│   ├── pitch.md                # Livrable 1
│   ├── conception.md           # Livrable 2
│   ├── bilan.md                # Livrable 3 (fin de projet)
│   ├── reversibilite.md        # Livrable 4 (fin de projet)
│   └── setup-copilot.md        # Guide de mise en place Copilot
├── src/                        # Code source Java
├── build.gradle                # Configuration Gradle (fourni)
└── README.md                   # Ce fichier
```

---

## En résumé

Vous êtes libres sur le choix de votre projet. Faites-vous plaisir ! Je vous demande juste de **bien coder et bien documenter**. Si le code est mauvais, je changerai vos features pour vous forcer à refactoriser — et ça vous éloignera de ce que vous vouliez faire.

Gardez cette illustration connue de la balançoire dans les arbres : 

![Image](https://cdn.zsite.net/us1/upload/201712/f_2a1ca764ce384a33d056f8256f6a1ce5.jpg)

Elle est souvent utilisée pour illustrer comment les projets échouent à cause d'une mauvaise communication entre le client et les développeurs. Par chance, vous êtes à la fois, le client (au début, ensuite ce sera moi), le développeur, le chef de projet, le testeur, etc. Vous avez donc le pouvoir de faire en sorte que la balançoire soit exactement ce que vous voulez, à condition de suivre une démarche rigoureuse.