package com.cabinetmedical.controller;

import com.cabinetmedical.service.AuthService;
import com.cabinetmedical.model.Utilisateur;
import com.cabinetmedical.util.AlertUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

// controle l'ecran du connexion
public class LoginController {

    @FXML
    private TextField champLogin;

    @FXML
    private PasswordField champMotDePasse;

    private AuthService authService = new AuthService();

    @FXML
    private void onConnexion(ActionEvent event) {
        String login = champLogin.getText();
        String motDePasse = champMotDePasse.getText();

        try {
            Utilisateur utilisateur = authService.connecter(login, motDePasse);
            ouvrirEcranPrincipal(event, utilisateur);

        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    private void ouvrirEcranPrincipal(ActionEvent event, Utilisateur utilisateur) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/main.fxml"));
        Parent racine = loader.load();

        MainController controller = loader.getController();
        controller.setUtilisateurConnecte(utilisateur);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }
}