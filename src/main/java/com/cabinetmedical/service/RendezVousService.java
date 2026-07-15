package com.cabinetmedical.service;


import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.model.RendezVous;
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
}
