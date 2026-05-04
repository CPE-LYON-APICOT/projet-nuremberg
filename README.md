---
binome:
  - nom: "Laïb"
    prenom: "Jessim"
  - nom: "Sumbo"
    prenom: "Daniel"
---

# Projet POO — 3ICS
NUREMBERG
 
A la toute fin, brièvement, décrivez le ici, comme si c'était la page de présentation sur un store d'applications. 



## Consignes

📋 [Lire les consignes du projet](Consignes.md)

## Github Copilot

📖 [Guide de mise en place de GitHub Copilot](docs/setup-copilot.md)

## API Web

Le projet expose aussi une API HTTP JSON pour une application web.

Lancement : `./gradlew runApi` ou `gradlew.bat runApi` sous Windows.

Base URL par défaut : `http://localhost:8080/api`

Interface web : `http://localhost:8080/`

Routes principales :

- `GET /health` : état du serveur
- `GET /formations` : liste des formations
- `POST /formations` : création d’une formation
- `GET /formations/{id}` : détail d’une formation
- `POST /formations/{id}/students` : inscription d’un étudiant
- `POST /formations/{id}/contents` : ajout d’un contenu pédagogique
- `POST /formations/{id}/contents/complete` : marquage d’un contenu comme terminé
- `GET /formations/{id}/progress` : progression de la formation
- `POST /formations/{id}/purchase` : achat + inscription

## Base SQL

Les scripts de base de données se trouvent dans [src/main/resources/sql](src/main/resources/sql).

- [schema.sql](src/main/resources/sql/schema.sql) : structure des tables, clés étrangères et index
- [data.sql](src/main/resources/sql/data.sql) : données de départ pour tester rapidement l'application

Pour lancer une base MySQL locale avec phpMyAdmin, utilisez [docker-compose.yml](docker-compose.yml) :

```bash
docker compose up --build -d
```

L'API se connecte ensuite à MySQL via les variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` et `DB_PASSWORD`.
Par défaut, elle vise `localhost:3306` avec la base `horizon_savoir`.
phpMyAdmin est exposé sur `http://localhost:8081/`.

L'API web est exposée sur `http://localhost:8080/`.

## Documents

| Livrable | Fichier | Quand |
|---|---|---|
| Pitch | [docs/pitch.md](docs/pitch.md) | Début de projet |
| Conception technique | [docs/conception.md](docs/conception.md) | Avant de coder |
| Bilan projet | [docs/bilan.md](docs/bilan.md) | Fin de projet |
| Réversibilité technique | [docs/reversibilite.md](docs/reversibilite.md) | Fin de projet |

## Contribuer

🛠️ **Avant de coder**, lisez [CONTRIBUTING.md](CONTRIBUTING.md) pour savoir quels fichiers modifier et lesquels ne pas toucher.


