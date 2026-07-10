package com.cabinetmedical.model;

import java.time.LocalDateTime;

public class Utilisateur {
    private int id,
    private String login,
    private String motDePasse,
    private Role role,
    private LocalDateTime dateCreation;

    public Utilisateur() {
    }

    public Utilisateur(int id, String login, String motDePasse, Role role, LocalDateTime dateCreation) {
        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", role=" + role +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
