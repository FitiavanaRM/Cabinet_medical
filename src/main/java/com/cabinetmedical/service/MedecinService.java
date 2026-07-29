package com.cabinetmedical.service;

import com.cabinetmedical.dao.MedecinDAO;
import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.StatutRendezVous;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

// service medecin
public class MedecinService {
    private MedecinDAO medecinDAO = new MedecinDAO();
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();

    // list
    public List<Medecin> listerTousMedecins() throws Exception {
        return medecinDAO.findAll();
    }

    // Recherche
    public Medecin rechercherMedecinParId(int id) throws Exception {
        return medecinDAO.findById(id);
    }

    // Ajoute un nouveau medecin, apres avoir valide ses donnees
    public void ajouterMedecin(Medecin medecin) throws Exception {
        validerMedecin(medecin);
        medecinDAO.insert(medecin);
    }

    // Modification
    public void modifierMedecin(Medecin medecin) throws Exception {
        Medecin existant = medecinDAO.findById(medecin.getId());
        if (existant == null) {
            throw new Exception("Medecin introuvable, impossible de le modifier.");
        }
        validerMedecin(medecin);
        medecinDAO.update(medecin);
    }

    // Suppression
    public void supprimerMedecin(int id) throws Exception {
        Medecin existant = medecinDAO.findById(id);
        if (existant == null) {
            throw new Exception("Medecin introuvable, impossible de le supprimer.");
        }

        List<RendezVous> rdvMedecin = rendezVousDAO.findByMedecin(id);
        for (RendezVous rdv : rdvMedecin) {
            if (rdv.getStatut() == StatutRendezVous.PREVU) {
                throw new Exception("Impossible de supprimer ce medecin car il a un rendez-vous a venir.");
            }
        }
        medecinDAO.delete(id);
    }

    // verification disponibilite medecin
    public boolean estDisponible(int idMedecin, LocalDate date, LocalTime heure) throws Exception {
        Medecin medecin = medecinDAO.findById(idMedecin);
        if (medecin == null) {
            throw new Exception("Medecin introuvable.");
        }

        // Jour
        String jourDemande = convertirJourEnFrancais(date.getDayOfWeek());
        List<String> joursDisponibles = Arrays.asList(medecin.getJoursDisponibles().split(","));
        if (!joursDisponibles.contains(jourDemande)) {
            return false;
        }

        // Heure
        if (heure.isBefore(medecin.getHeureDebutDispo()) || heure.isAfter(medecin.getHeureFinDispo())) {
            return false;
        }
        return true;
    }

    // Verification des donnees obligatoire
    private void validerMedecin(Medecin medecin) throws Exception {
        if (medecin.getNom() == null || medecin.getNom().trim().isEmpty()) {
            throw new Exception("Le nom du medecin est obligatoire.");
        }
        if (medecin.getPrenom() == null || medecin.getPrenom().trim().isEmpty()) {
            throw new Exception("Le prenom du medecin est obligatoire.");
        }
        if (medecin.getSpecialite() == null || medecin.getSpecialite().trim().isEmpty()) {
            throw new Exception("La specialite du medecin est obligatoire.");
        }
        if (medecin.getTelephone() == null || medecin.getTelephone().trim().isEmpty()) {
            throw new Exception("Le telephone du medecin est obligatoire.");
        }
        if (medecin.getJoursDisponibles() == null || medecin.getJoursDisponibles().trim().isEmpty()) {
            throw new Exception("Les jours de disponibilite sont obligatoires.");
        }
        if (medecin.getHeureDebutDispo() == null || medecin.getHeureFinDispo() == null) {
            throw new Exception("Les horaires de disponibilite sont obligatoires.");
        }
        if (!medecin.getHeureDebutDispo().isBefore(medecin.getHeureFinDispo())) {
            throw new Exception("L'heure de debut doit etre avant l'heure de fin.");
        }
    }

    // conversion des jour dans JAVA en frs
    private String convertirJourEnFrancais(DayOfWeek jour) {
        switch (jour) {
            case MONDAY: return "Lundi";
            case TUESDAY: return "Mardi";
            case WEDNESDAY: return "Mercredi";
            case THURSDAY: return "Jeudi";
            case FRIDAY: return "Vendredi";
            case SATURDAY: return "Samedi";
            case SUNDAY: return "Dimanche";
            default: return "";
        }
    }
}