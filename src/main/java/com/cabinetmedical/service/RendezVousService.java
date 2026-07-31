package com.cabinetmedical.service;


import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.StatutRendezVous;

import java.time.LocalDateTime;
import java.util.List;

public class RendezVousService {
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();
    private MedecinService medecinService = new MedecinService();

    // liste de tous les rdv
    public List<RendezVous> listeRdv() throws Exception {
        return rendezVousDAO.findAll();
    }

    // rechercher un rdv par son Id
    public RendezVous rechercheRendezVousParId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("L'identifiant du rendez-vous est invalide.");
        }
        return rendezVousDAO.findById(id);
    }

    // donne le rendez vous du patient
    public List<RendezVous> listeRendezVousPatient(int idPatient) throws Exception {
        return rendezVousDAO.findByPatient(idPatient);
    }

    // donne le liste rdv du medecin
    public List<RendezVous> listeRendezVousMedecin(int idMedecin) throws Exception {
        return rendezVousDAO.findByMedecin(idMedecin);
    }

    // creation d'un nouveau rendez vous
    public void creerRdv(RendezVous rdv) throws Exception {
        validationRdv(rdv);

        // disponibilite du medecin
        if (!medecinService.estDisponible(rdv.getIdMedecin(), rdv.getDateRdv(), rdv.getHeureRdv())) {
            throw new Exception("Le medecin n'est pas disponible a cette date et cette heure.");
        }
        if (rendezVousDAO.existeChevauchement(rdv.getIdMedecin(), rdv.getDateRdv(), rdv.getHeureRdv())) {
            throw new Exception("Le medecin a deja un rendez-vous a cette date et cette heure.");
        }

        rdv.setStatut(StatutRendezVous.PREVU);
        rendezVousDAO.insert(rdv);
    }

    // modification d'un rdv
    public void modifRdv(RendezVous rdv) throws Exception {
        RendezVous existant = rendezVousDAO.findById(rdv.getId());
        if (existant == null) {
            throw new Exception("Rendez-vous introuvable donc c'est impossible de le modifier");
        }

        validationRdv(rdv);
        if (!medecinService.estDisponible(rdv.getIdMedecin(), rdv.getDateRdv(), rdv.getHeureRdv())) {
            throw new Exception("Le medecin n'est pas disponible a cette date et cette heure");
        }

        boolean creneauChange = existant.getIdMedecin() != rdv.getIdMedecin() || !existant.getDateRdv().equals(rdv.getDateRdv()) || !existant.getHeureRdv().equals(rdv.getHeureRdv());

        if (creneauChange && rendezVousDAO.existeChevauchement(rdv.getIdMedecin(), rdv.getDateRdv(), rdv.getHeureRdv())) {
            throw new Exception("Le medecin a deja un rendez-vous a cette date et cette heure.");
        }

        rendezVousDAO.update(rdv);
    }

    // suppression definitive
    public void supprimerRendezVous(int id) throws Exception {
        RendezVous existant = rendezVousDAO.findById(id);
        if (existant == null) {
            throw new Exception("Rendez-vous introuvable, impossible de le supprimer.");
        }
        rendezVousDAO.delete(id);
    }

    // Changement statut (PREVU en HONORE ou ANNULE)
    public void changerStatut(int idRdv, StatutRendezVous nouveauStatut) throws Exception {
        RendezVous rdv = rendezVousDAO.findById(idRdv);
        if (rdv == null) {
            throw new Exception("Rendez-vous introuvable.");
        }

        if (rdv.getStatut() != StatutRendezVous.PREVU) {
            throw new Exception("Seul un rendez-vous PREVU peut changer de statut.");
        }

        rdv.setStatut(nouveauStatut);
        rendezVousDAO.update(rdv);
    }

    // validation
    private void validationRdv(RendezVous rdv) throws Exception {
        if (rdv.getIdPatient() <= 0) {
            throw new Exception("Le patient est obligatoire");
        }

        if (rdv.getIdMedecin() <= 0) {
            throw new Exception("Le medecin est obligatoire");
        }

        if (rdv.getDateRdv() == null || rdv.getHeureRdv() == null) {
            throw new Exception("La date et l'heure du rendez vous sont obligatoire");
        }

        LocalDateTime dateHeureRdv = LocalDateTime.of(rdv.getDateRdv(), rdv.getHeureRdv());
        if (dateHeureRdv.isBefore(LocalDateTime.now())) {
            throw new Exception("Impossible de creer un rendez vous dans le passé");
        }
    }
}
