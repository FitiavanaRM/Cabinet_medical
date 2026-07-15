package com.cabinetmedical.service;


import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.model.RendezVous;

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
    public List<RendezVous> rechercheRendezVousParId(int id) throws Exception {
        return rendezVousDAO.findAll();
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
