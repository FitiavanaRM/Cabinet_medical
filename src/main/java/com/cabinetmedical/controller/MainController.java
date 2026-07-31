package com.cabinetmedical.controller;

import com.cabinetmedical.model.Utilisateur;
import com.cabinetmedical.util.ThemeManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Label labelUtilisateurConnecte;

    @FXML
    private Button boutonTheme;

    private Utilisateur utilisateurConnecte;

    @FXML
    private void initialize() {
        mettreAJourBoutonTheme();
    }

    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        labelUtilisateurConnecte.setText("Connecte : " + utilisateur.getLogin() + " (" + utilisateur.getRole() + ")");
    }

    @FXML
    private void onThemeToggle(ActionEvent event) {
        ThemeManager.toggle();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ThemeManager.applyToScene(stage.getScene());
        mettreAJourBoutonTheme();
    }

    private void mettreAJourBoutonTheme() {
        if (boutonTheme == null) {
            return;
        }

        boutonTheme.setText(ThemeManager.isDark() ? "Mode clair" : "Mode sombre");
    }

    private void ouvrirEcran(String cheminFxml, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFxml));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(racine);
        ThemeManager.applyToScene(scene);
        stage.setScene(scene);
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }

    @FXML
    private void onPatients(ActionEvent event) throws IOException {
        ouvrirEcran("/com/cabinetmedical/fxml/patient.fxml", event);
    }

    @FXML
    private void onMedecins(ActionEvent event) throws IOException {
        ouvrirEcran("/com/cabinetmedical/fxml/medecin.fxml", event);
    }

    @FXML
    private void onRendezVous(ActionEvent event) throws IOException {
        ouvrirEcran("/com/cabinetmedical/fxml/rendezvous.fxml", event);
    }

    @FXML
    private void onConsultations(ActionEvent event) throws IOException {
        ouvrirEcran("/com/cabinetmedical/fxml/consultation.fxml", event);
    }

    @FXML
    private void onRapports(ActionEvent event) throws IOException {
        ouvrirEcran("/com/cabinetmedical/fxml/rapport.fxml", event);
    }

    // Ferme la session et revient a l'ecran de connexion
    @FXML
    private void onDeconnexion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/login.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(racine);
        ThemeManager.applyToScene(scene);
        stage.setScene(scene);
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }
}
