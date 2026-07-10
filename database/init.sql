-- creation de la base de donnees

CREATE DATABASE IF NOT EXISTS cabinet_medical;
USE cabinet_medical;

CREATE TABLE utilisateur (
    id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(200) NOT NULL,
    role ENUM('ADMIN', 'SECRETAIRE', 'MEDECIN') NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE medecin (
    id_medecin INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    specialite VARCHAR(100) NOT NULL,
    telephone VARCHAR(50),
    email VARCHAR(100),
    jours_disponibles VARCHAR(100) NOT NULL DEFAULT 'Lundi, Mardi, Mercredi, Jeudi, Vendredi',
    heure_debut_dispo TIME NOT NULL DEFAULT '08:00:00',
    heure_fin_dispo TIME NOT NULL DEFAULT '18:00:00',

    CONSTRAINT fk_medecin_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE patient (
    id_patient INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance  DATE NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    adresse VARCHAR(255),
    antecedents TEXT
);

REATE TABLE rendez_vous (
    id_rdv INT AUTO_INCREMENT PRIMARY KEY,
    id_patient INT NOT NULL,
    id_medecin INT NOT NULL,
    date_rdv DATE NOT NULL,
    heure_rdv TIME NOT NULL,
    motif VARCHAR(255),
    statut ENUM('PREVU', 'HONORE', 'ANNULE') NOT NULL DEFAULT 'PREVU',

    CONSTRAINT fk_rdv_patient FOREIGN KEY (id_patient) REFERENCES patient(id_patient)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_rdv_medecin FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT uq_medecin_creneau UNIQUE (id_medecin, date_rdv, heure_rdv)
);

CREATE TABLE consultation (
    id_consultation INT AUTO_INCREMENT PRIMARY KEY,
    id_rdv INT NOT NULL UNIQUE,
    diagnostic TEXT,
    ordonnance TEXT,
    date_consultation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_consultation_rdv FOREIGN KEY (id_rdv) REFERENCES rendez_vous(id_rdv)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- insertion donnees (test zani)
INSERT INTO utilisateur (login, mot_de_passe, role) VALUES
    ('admin', 'admin123', 'ADMIN'),
    ('secretaire', 'secret123', 'SECRETAIRE'),
    ('Dr Hery', 'medecin123', 'MEDECIN');

INSERT INTO medecin (id_utilisateur, nom, prenom, specialite, telephone, email, jours_disponibles, heure_debut_dispo, heure_fin_dispo) VALUES
    (NULL, 'Rotsy', 'Nomena', 'Medecin generale', '0389154855', 'rotsynomena9@gmail.com', 'Lundi, Mardi, Mercredi, Jeudi, Vendredi', '08:00:00', '18:00:00'),
    (3, 'Hery', 'Joseph', 'Cardiologie', '0341326685', 'heryjoseph@gmail.com', 'Lundi, Mercredi, Vendredi', '09:00:00', '16:00:00');

INSERT INTO patient (nom, prenom, date_naissance, telephone, adresse, antecedents) VALUES
    ('Malalaniavo', 'Valisoa', '2005-02-03', '0381906779', 'Ambihibary', 'Asthme leger'),
    ('Rakotomanantsoa', 'Yanki', '2007-01-08', '0344838065', 'Boys_Dorm', 'Aucun antecedent connu');

INSERT INTO rendez_vous (id_patient, id_medecin, date_rdv, heure_rdv, motif, statut) VALUES
    (1, 2, '2026-07-29', '15:00:00', 'Controle cardiage', 'PREVU'),
    (2, 1, '2026-07-30', '10:00:00', 'Crise d asthme', 'HONORE');

INSERT INTO consultation (id_rdv, diagnostic, ordonnance) VALUES
    (2, 'Crise d asthme moderee', 'Ventoline 100mcg, 2 bouffees, maximum 4 fois par jour');
