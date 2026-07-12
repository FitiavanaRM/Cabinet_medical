package com.cabinetmedical.service;

import com.cabinetmedical.dao.PatientDAO;
import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.dao.ConsultationDAO;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.StatutRendezVous;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// service du patient
public class PatientService {
    private PatientDAO patientDAO = new PatientDAO();
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();
    private ConsultationDAO consultationDAO = new ConsultationDAO();

    // list
    public List<Patient> listerTousPatients() throws Exception {
        return patientDAO.findAll();
    }

    // Recherche
    public Patient rechercherPatientParId(int id) throws Exception {
        return patientDAO.findById(id);
    }

    // Ajout
    public void ajouterPatient(Patient patient) throws Exception {
        validerPatient(patient);
        patientDAO.insert(patient);
    }

    // Modification
    public void modifierPatient(Patient patient) throws Exception {
        Patient existant = patientDAO.findById(patient.getId());
        if (existant == null) {
            throw new Exception("Patient introuvable, impossible de le modifier.");
        }
        validerPatient(patient);
        patientDAO.update(patient);
    }

    // Suppression
    public void supprimerPatient(int id) throws Exception {
        Patient existant = patientDAO.findById(id);
        if (existant == null) {
            throw new Exception("Patient introuvable, impossible de le supprimer.");
        }

        List<RendezVous> rdvPatient = rendezVousDAO.findByPatient(id);
        for (RendezVous rdv : rdvPatient) {
            if (rdv.getStatut() == StatutRendezVous.PREVU) {
                throw new Exception("Impossible de supprimer ce patient car il a un rendez-vous a venir.");
            }
        }
        patientDAO.delete(id);
    }

    // historique
    public List<String> construireHistoriqueMedical(int idPatient) throws Exception {
        List<String> historique = new ArrayList<>();
        List<RendezVous> rdvPatient = rendezVousDAO.findByPatient(idPatient);

        for (RendezVous rdv : rdvPatient) {
            String ligne = "Le " + rdv.getDateRdv() + " a " + rdv.getHeureRdv()
                    + " - Motif : " + rdv.getMotif()
                    + " - Statut : " + rdv.getStatut();

            if (rdv.getStatut() == StatutRendezVous.HONORE) {
                Consultation consultation = consultationDAO.findByRdv(rdv.getId());
                if (consultation != null) {
                    ligne += " - Diagnostic : " + consultation.getDiagnostic()
                            + " - Ordonnance : " + consultation.getOrdonnance();
                }
            }
            historique.add(ligne);
        }
        return historique;
    }

    // Verification des donnees obligatoire
    private void validerPatient(Patient patient) throws Exception {
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            throw new Exception("Le nom du patient est obligatoire.");
        }
        if (patient.getPrenom() == null || patient.getPrenom().trim().isEmpty()) {
            throw new Exception("Le prenom du patient est obligatoire.");
        }
        if (patient.getDateNaissance() == null) {
            throw new Exception("La date de naissance est obligatoire.");
        }

        // si la date est dans le future
        if (patient.getDateNaissance().isAfter(LocalDate.now())) {
            throw new Exception("La date de naissance est invalide.");
        }
        if (patient.getTelephone() == null || patient.getTelephone().trim().isEmpty()) {
            throw new Exception("Le telephone du patient est obligatoire.");
        }
    }
}