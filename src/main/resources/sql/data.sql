INSERT INTO formations (id, titre, description, prix) VALUES
(1, 'Formation Java', 'Une formation complète sur Java', 199.99);

INSERT INTO etudiants (nom, prenom, adresse, date_naissance, email) VALUES
('Dupont', 'Jean', '123 rue Paris', DATE '2000-01-15', 'jean@email.com'),
('Martin', 'Marie', '456 rue Lyon', DATE '2001-03-14', 'marie@email.com');

INSERT INTO contenus_pedagogiques (formation_id, type_contenu, titre, est_termine, valeur_specifique, ordre_affichage) VALUES
(1, 'video', 'Les bases Java', FALSE, 45, 1),
(1, 'quiz', 'Quiz 1', FALSE, 70, 2),
(1, 'pdf', 'Cours 1', FALSE, 25, 3);

INSERT INTO syllabus (formation_id) VALUES (1);

INSERT INTO syllabus_chapitres (syllabus_id, titre, ordre_affichage) VALUES
(1, 'Chapitre 1 : Variables', 1),
(1, 'Chapitre 2 : Boucles', 2),
(1, 'Chapitre 3 : POO', 3);

INSERT INTO syllabus_objectifs (syllabus_id, objectif, ordre_affichage) VALUES
(1, 'Comprendre les bases de Java', 1),
(1, 'Savoir structurer une application simple', 2),
(1, 'Manipuler des classes et objets', 3);

INSERT INTO inscriptions (formation_id, etudiant_id, progression, statut) VALUES
(1, 1, 0, 'active'),
(1, 2, 0, 'active');
