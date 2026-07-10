package com.cabinetmedical.model;

import java.time.LocalDateTime;

public class Consultation {
    private int id;
    private int idRdv;
    private String diagnostic;
    private String ordonnance;
    private LocalDateTime dateConsultation;

    public Consultation() {
    }

    public Consultation(int id, int idRdv, String diagnostic, String ordonnance, LocalDateTime dateConsultation) {
        this.id = id;
        this.idRdv = idRdv;
        this.diagnostic = diagnostic;
        this.ordonnance = ordonnance;
        this.dateConsultation = dateConsultation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRdv() {
        return idRdv;
    }

    public void setIdRdv(int idRdv) {
        this.idRdv = idRdv;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(String ordonnance) {
        this.ordonnance = ordonnance;
    }

    public LocalDateTime getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDateTime dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", idRdv=" + idRdv +
                ", diagnostic='" + diagnostic + '\'' +
                ", ordonnance='" + ordonnance + '\'' +
                ", dateConsultation=" + dateConsultation +
                '}';
    }
}
