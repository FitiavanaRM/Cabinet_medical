package com.cabinetmedical.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVous {
    private int id;
    private int idPatient;
    private int idMedecin;
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String motif;
    private StatutRendezVous statut;

    public RendezVous() {
    }

    public RendezVous(int id, int idPatient, int idMedecin, LocalDate dateRdv, LocalTime heureRdv, String motif, StatutRendezVous statut) {
        this.id = id;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.dateRdv = dateRdv;
        this.heureRdv = heureRdv;
        this.motif = motif;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(int idMedecin) {
        this.idMedecin = idMedecin;
    }

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(LocalDate dateRdv) {
        this.dateRdv = dateRdv;
    }

    public LocalTime getHeureRdv() {
        return heureRdv;
    }

    public void setHeureRdv(LocalTime heureRdv) {
        this.heureRdv = heureRdv;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public StatutRendezVous getStatut() {
        return statut;
    }

    public void setStatut(StatutRendezVous statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", idPatient=" + idPatient +
                ", idMedecin=" + idMedecin +
                ", dateRdv=" + dateRdv +
                ", heureRdv=" + heureRdv +
                ", motif='" + motif + '\'' +
                ", statut=" + statut +
                '}';
    }
}
