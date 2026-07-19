package com.cabinetmedical.service;

import com.cabinetmedical.dao.UtilisateurDAO;
import com.cabinetmedical.model.Utilisateur;
import com.cabinetmedical.model.Role;
import com.cabinetmedical.util.PasswordUtil;

// authentification des comptes
public class AuthService {
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    // Verifie le login et le mot de passe saisis par l'utilisateur.
    public Utilisateur connecter(String login, String motDePasseSaisi) throws Exception {
        if (login == null || login.trim().isEmpty()) {
            throw new Exception("Le login est obligatoire.");
        }
        if (motDePasseSaisi == null || motDePasseSaisi.trim().isEmpty()) {
            throw new Exception("Le mot de passe est obligatoire.");
        }

        // On cherche le compte correspondant
        Utilisateur utilisateur = utilisateurDAO.findByLogin(login);
        if (utilisateur == null) {
            throw new Exception("Login ou mot de passe incorrect.");
        }

        // Comparaison de hash
        boolean motDePasseIzy = PasswordUtil.matches(motDePasseSaisi, utilisateur.getMotDePasse());
        if (!motDePasseIzy) {
            throw new Exception("Login ou mot de passe incorrect.");
        }
        return utilisateur;
    }

    // creation nouveau compte
    public void creerCompte(String login, String motDePasseClair, Role role) throws Exception {
        if (login == null || login.trim().isEmpty()) {
            throw new Exception("Le login est obligatoire.");
        }
        if (motDePasseClair == null || motDePasseClair.trim().isEmpty()) {
            throw new Exception("Le mot de passe est obligatoire.");
        }
        if (role == null) {
            throw new Exception("Le role est obligatoire.");
        }

        // deux utilisateurs ne peuvent pas avoir le meme login
        Utilisateur existant = utilisateurDAO.findByLogin(login);
        if (existant != null) {
            throw new Exception("Ce login est deja utilise par un autre compte.");
        }

        // hachage mot de passe
        String motDePasseHache = PasswordUtil.hash(motDePasseClair);

        Utilisateur nouvelUtilisateur = new Utilisateur();
        nouvelUtilisateur.setLogin(login);
        nouvelUtilisateur.setMotDePasse(motDePasseHache);
        nouvelUtilisateur.setRole(role);

        // insertion
        utilisateurDAO.insert(nouvelUtilisateur);
    }
}