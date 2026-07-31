package com.cabinetmedical.controller;

import com.cabinetmedical.model.Utilisateur;
import com.cabinetmedical.util.AlertUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Label labelUtilisateurConnecte;
    private Utilisateur utilisateurConnecte;

    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        labelUtilisateurConnecte.setText("Connecte : " + utilisateur.getLogin() + " (" + utilisateur.getRole() + ")");
    }

    @FXML
    private void onPatients(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/patient.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }

    @FXML
    private void onMedecins(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/medecin.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }

    @FXML
    private void onRendezVous(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/rendezvous.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }

    @FXML
    private void onConsultations(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/consultation.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }

    @FXML
    private void onRapports(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/rapport.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }

    // Ferme la session et revient a l'ecran de connexion
    @FXML
    private void onDeconnexion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/login.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }
}
