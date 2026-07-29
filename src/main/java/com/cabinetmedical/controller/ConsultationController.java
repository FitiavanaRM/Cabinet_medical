package com.cabinetmedical.controller;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.service.ConsultationService;
import com.cabinetmedical.util.AlertUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// controle l'ecran des consultations
public class ConsultationController implements Initializable {

    @FXML private TableView<Consultation> tableConsultations;
    @FXML private TableColumn<Consultation, Integer> colId;
    @FXML private TableColumn<Consultation, Integer> colRdv;
    @FXML private TableColumn<Consultation, String> colDiagnostic;
    @FXML private TableColumn<Consultation, Object> colDateConsultation;

    @FXML private TextField champRdv;
    @FXML private TextArea champDiagnostic;
    @FXML private TextArea champOrdonnance;

    private ConsultationService consultationService = new ConsultationService();
    private ObservableList<Consultation> listeConsultations = FXCollections.observableArrayList();
    private Consultation consultationSelectionnee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRdv.setCellValueFactory(new PropertyValueFactory<>("idRdv"));
        colDiagnostic.setCellValueFactory(new PropertyValueFactory<>("diagnostic"));
        colDateConsultation.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));

        tableConsultations.setItems(listeConsultations);
        tableConsultations.getSelectionModel().selectedItemProperty().addListener((observable, ancien, nouveau) -> {
            if (nouveau != null) {
                remplirFormulaire(nouveau);
            }
        });

        chargerConsultations();
    }

    // Recupere les consultations depuis ConsultationService
    private void chargerConsultations() {
        try {
            listeConsultations.setAll(consultationService.listerToutesConsultations());
        } catch (Exception e) {
            AlertUtil.afficherErreur("Impossible de charger les consultations : " + e.getMessage());
        }
    }

    // Remplit le formulaire avec les donnees de la consultation choisie
    private void remplirFormulaire(Consultation consultation) {
        consultationSelectionnee = consultation;
        champRdv.setText(String.valueOf(consultation.getIdRdv()));
        champDiagnostic.setText(consultation.getDiagnostic());
        champOrdonnance.setText(consultation.getOrdonnance());
    }

    // Vide le formulaire pour la saisie d'une nouvelle consultation
    @FXML
    private void onNouveau() {
        consultationSelectionnee = null;
        champRdv.clear();
        champDiagnostic.clear();
        champOrdonnance.clear();
        tableConsultations.getSelectionModel().clearSelection();
    }

    // Construit une consultation a partir des champs du formulaire
    private Consultation construireConsultationDepuisFormulaire() {
        Consultation consultation = new Consultation();

        if (consultationSelectionnee != null) {
            consultation.setId(consultationSelectionnee.getId());
        }

        try {
            consultation.setIdRdv(Integer.parseInt(champRdv.getText()));
        } catch (NumberFormatException e) {
            AlertUtil.afficherErreur("L'identifiant du rendez-vous doit etre un nombre entier.");
            return null;
        }

        consultation.setDiagnostic(champDiagnostic.getText());
        consultation.setOrdonnance(champOrdonnance.getText());
        return consultation;
    }

    // Ajoute une consultation pour un rendez-vous honore
    @FXML
    private void onAjouter() {
        Consultation consultation = construireConsultationDepuisFormulaire();
        if (consultation == null) {
            return;
        }

        try {
            consultationService.creerConsultation(consultation);
            AlertUtil.afficherSucces("Consultation ajoutee avec succes.");
            chargerConsultations();
            onNouveau();
        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    // Modifie la consultation selectionnee
    @FXML
    private void onModifier() {
        if (consultationSelectionnee == null) {
            AlertUtil.afficherErreur("Selectionnez d'abord une consultation dans le tableau.");
            return;
        }

        Consultation consultation = construireConsultationDepuisFormulaire();
        if (consultation == null) {
            return;
        }

        try {
            consultationService.modifierConsultation(consultation);
            AlertUtil.afficherSucces("Consultation modifiee avec succes.");
            chargerConsultations();
            onNouveau();
        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    // Supprime la consultation selectionnee
    @FXML
    private void onSupprimer() {
        if (consultationSelectionnee == null) {
            AlertUtil.afficherErreur("Selectionnez d'abord une consultation dans le tableau.");
            return;
        }

        try {
            consultationService.supprimerConsultation(consultationSelectionnee.getId());
            AlertUtil.afficherSucces("Consultation supprimee avec succes.");
            chargerConsultations();
            onNouveau();
        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    // Revient a l'ecran principal
    @FXML
    private void onRetour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/main.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }
}
