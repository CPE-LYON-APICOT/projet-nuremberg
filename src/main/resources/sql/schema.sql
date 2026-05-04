CREATE TABLE formations (
    id INTEGER PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    prix DECIMAL(10,2) NOT NULL CHECK (prix >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE etudiants (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom VARCHAR(120) NOT NULL,
    prenom VARCHAR(120) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    date_naissance DATE NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contenus_pedagogiques (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    formation_id INTEGER NOT NULL,
    type_contenu VARCHAR(30) NOT NULL CHECK (type_contenu IN ('video', 'quiz', 'pdf')),
    titre VARCHAR(255) NOT NULL,
    est_termine BOOLEAN NOT NULL DEFAULT FALSE,
    valeur_specifique INTEGER NOT NULL DEFAULT 0,
    ordre_affichage INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contenu_formation
        FOREIGN KEY (formation_id)
        REFERENCES formations(id)
        ON DELETE CASCADE
);

CREATE TABLE syllabus (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    formation_id INTEGER NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_syllabus_formation
        FOREIGN KEY (formation_id)
        REFERENCES formations(id)
        ON DELETE CASCADE
);

CREATE TABLE syllabus_chapitres (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    syllabus_id INTEGER NOT NULL,
    titre VARCHAR(255) NOT NULL,
    ordre_affichage INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_syllabus_chapitre
        FOREIGN KEY (syllabus_id)
        REFERENCES syllabus(id)
        ON DELETE CASCADE
);

CREATE TABLE syllabus_objectifs (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    syllabus_id INTEGER NOT NULL,
    objectif VARCHAR(255) NOT NULL,
    ordre_affichage INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_syllabus_objectif
        FOREIGN KEY (syllabus_id)
        REFERENCES syllabus(id)
        ON DELETE CASCADE
);

CREATE TABLE inscriptions (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    formation_id INTEGER NOT NULL,
    etudiant_id INTEGER NOT NULL,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    progression DECIMAL(5,2) NOT NULL DEFAULT 0,
    statut VARCHAR(30) NOT NULL DEFAULT 'active',
    CONSTRAINT fk_inscription_formation
        FOREIGN KEY (formation_id)
        REFERENCES formations(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_inscription_etudiant
        FOREIGN KEY (etudiant_id)
        REFERENCES etudiants(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_inscription UNIQUE (formation_id, etudiant_id)
);

CREATE TABLE paiements (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    formation_id INTEGER NOT NULL,
    etudiant_id INTEGER NOT NULL,
    order_id VARCHAR(80) NOT NULL UNIQUE,
    provider VARCHAR(30) NOT NULL CHECK (provider IN ('stripe', 'paypal')),
    currency CHAR(3) NOT NULL,
    amount_cents BIGINT NOT NULL CHECK (amount_cents >= 0),
    transaction_id VARCHAR(120),
    success BOOLEAN NOT NULL DEFAULT FALSE,
    payment_status VARCHAR(40) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_paiement_formation
        FOREIGN KEY (formation_id)
        REFERENCES formations(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_paiement_etudiant
        FOREIGN KEY (etudiant_id)
        REFERENCES etudiants(id)
        ON DELETE CASCADE
);

CREATE TABLE notifications (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    formation_id INTEGER NOT NULL,
    etudiant_id INTEGER,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lu BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_notification_formation
        FOREIGN KEY (formation_id)
        REFERENCES formations(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_notification_etudiant
        FOREIGN KEY (etudiant_id)
        REFERENCES etudiants(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_contenus_formation ON contenus_pedagogiques(formation_id);
CREATE INDEX idx_inscriptions_formation ON inscriptions(formation_id);
CREATE INDEX idx_inscriptions_etudiant ON inscriptions(etudiant_id);
CREATE INDEX idx_paiements_formation ON paiements(formation_id);
CREATE INDEX idx_notifications_formation ON notifications(formation_id);
