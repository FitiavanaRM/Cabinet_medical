package com.cabinetmedical.model;

import java.time.LocalTime;

public class Medecin {
    private int id;
    private Integer idUtilisateur;
    private String nom;
    private String prenom;
    private String specialite;
    private String email;
    private String joursDisponibles;
    private LocalTime heureDebutDispo;
    private LocalTime heureFinDispo;

    public Medecin() {
    }

    public Medecin(int id, Integer idUtilisateur, String nom, String prenom, String specialite, String email, String joursDisponibles, LocalTime heureDebutDispo, LocalTime heureFinDispo) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.email = email;
        this.joursDisponibles = joursDisponibles;
        this.heureDebutDispo = heureDebutDispo;
        this.heureFinDispo = heureFinDispo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoursDisponibles() {
        return joursDisponibles;
    }

    public void setJoursDisponibles(String joursDisponibles) {
        this.joursDisponibles = joursDisponibles;
    }

    public LocalTime getHeureDebutDispo() {
        return heureDebutDispo;
    }

    public void setHeureDebutDispo(LocalTime heureDebutDispo) {
        this.heureDebutDispo = heureDebutDispo;
    }

    public LocalTime getHeureFinDispo() {
        return heureFinDispo;
    }

    public void setHeureFinDispo(LocalTime heureFinDispo) {
        this.heureFinDispo = heureFinDispo;
    }

    @Override
    public String toString() {
        return "Medecin{" +
                "id=" + id +
                ", idUtilisateur=" + idUtilisateur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", specialite='" + specialite + '\'' +
                ", email='" + email + '\'' +
                ", joursDisponibles='" + joursDisponibles + '\'' +
                ", heureDebutDispo=" + heureDebutDispo +
                ", heureFinDispo=" + heureFinDispo +
                '}';
    }
}
