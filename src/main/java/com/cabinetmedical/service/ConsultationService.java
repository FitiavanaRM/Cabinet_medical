package com.cabinetmedical.service;

import com.cabinetmedical.dao.ConsultationDAO;
import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.StatutRendezVous;

import java.util.List;

// service du consultation
public class ConsultationService {

    private ConsultationDAO consultationDAO = new ConsultationDAO();
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();

    // list
    public List<Consultation> listerToutesConsultations() throws Exception {
        return consultationDAO.findAll();
    }

    // Recherche par ID consultation
    public Consultation rechercherConsultationParId(int id) throws Exception {
        return consultationDAO.findById(id);
    }

    // creation nouvel consultation
    public void creerConsultation(Consultation consultation) throws Exception {
        validerConsultation(consultation);

        RendezVous rdv = rendezVousDAO.findById(consultation.getIdRdv());
        if (rdv == null) {
            throw new Exception("Rendez-vous introuvable.");
        }

        if (rdv.getStatut() != StatutRendezVous.HONORE) {
            throw new Exception("Une consultation ne peut etre creee que pour un rendez-vous HONORE.");
        }

        if (consultationDAO.existsForRdv(consultation.getIdRdv())) {
            throw new Exception("Ce rendez-vous a deja une consultation associee.");
        }

        consultationDAO.insert(consultation);
    }

    // modification
    public void modifierConsultation(Consultation consultation) throws Exception {
        Consultation existante = consultationDAO.findById(consultation.getId());
        if (existante == null) {
            throw new Exception("Consultation introuvable donc impossible de la modifier.");
        }
        validerConsultation(consultation);
        consultationDAO.update(consultation);
    }

    // Suppression
    public void supprimerConsultation(int id) throws Exception {
        Consultation existante = consultationDAO.findById(id);
        if (existante == null) {
            throw new Exception("Consultation introuvable et c'est impossible de la supprimer.");
        }
        consultationDAO.delete(id);
    }

    // Validation
    private void validerConsultation(Consultation consultation) throws Exception {
        if (consultation.getIdRdv() <= 0) {
            throw new Exception("Le rendez-vous associe est obligatoire.");
        }
        if (consultation.getDiagnostic() == null || consultation.getDiagnostic().trim().isEmpty()) {
            throw new Exception("Le diagnostic est obligatoire.");
        }
    }
}