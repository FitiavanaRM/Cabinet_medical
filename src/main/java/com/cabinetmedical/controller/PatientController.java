package com.cabinetmedical.controller;

import com.cabinetmedical.model.Patient;
import com.cabinetmedical.service.PatientService;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// controle l'ecran du patient

public class PatientController implements Initializable {

    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, Object> colDateNaissance;
    @FXML private TableColumn<Patient, String> colTelephone;

    @FXML private TextField champNom;
    @FXML private TextField champPrenom;
    @FXML private DatePicker champDateNaissance;
    @FXML private TextField champTelephone;
    @FXML private TextField champAdresse;
    @FXML private TextArea champAntecedents;

    private PatientService patientService = new PatientService();
    private ObservableList<Patient> listePatients = FXCollections.observableArrayList();
    private Patient patientSelectionne;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        tablePatients.setItems(listePatients);

        tablePatients.getSelectionModel().selectedItemProperty().addListener((observable, ancien, nouveau) -> {
            if (nouveau != null) {
                remplirFormulaire(nouveau);
            }
        });

        chargerPatients();
    }

    // Recupere tous les patients depuis PatientService
    private void chargerPatients() {
        try {
            listePatients.setAll(patientService.listerTousPatients());
        } catch (Exception e) {
            AlertUtil.afficherErreur("Impossible de charger les patients : " + e.getMessage());
        }
    }

    // Remplit les champs du formulaire avec les donnees d'un patient existant
    private void remplirFormulaire(Patient patient) {
        patientSelectionne = patient;
        champNom.setText(patient.getNom());
        champPrenom.setText(patient.getPrenom());
        champDateNaissance.setValue(patient.getDateNaissance());
        champTelephone.setText(patient.getTelephone());
        champAdresse.setText(patient.getAdresse());
        champAntecedents.setText(patient.getAntecedents());
    }

    // Vide le formulaire et deselectionne le tableau, pour saisir un nouveau patient
    @FXML
    private void onNouveau() {
        patientSelectionne = null;
        champNom.clear();
        champPrenom.clear();
        champDateNaissance.setValue(null);
        champTelephone.clear();
        champAdresse.clear();
        champAntecedents.clear();
        tablePatients.getSelectionModel().clearSelection();
    }

    // Construit un objet Patient a partir de ce qui est saisi dans le formulaire
    private Patient construirePatientDepuisFormulaire() {
        Patient patient = new Patient();

        // Si on modifie un patient existant, on garde son id d'origine
        if (patientSelectionne != null) {
            patient.setId(patientSelectionne.getId());
        }

        patient.setNom(champNom.getText());
        patient.setPrenom(champPrenom.getText());
        patient.setDateNaissance(champDateNaissance.getValue());
        patient.setTelephone(champTelephone.getText());
        patient.setAdresse(champAdresse.getText());
        patient.setAntecedents(champAntecedents.getText());

        return patient;
    }

    // Ajoute un nouveau patient a partir du formulaire
    @FXML
    private void onAjouter() {
        try {
            Patient patient = construirePatientDepuisFormulaire();
            patientService.ajouterPatient(patient);

            AlertUtil.afficherSucces("Patient ajoute avec succes.");
            chargerPatients();
            onNouveau();

        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    // Modifie le patient actuellement selectionne dans le tableau
    @FXML
    private void onModifier() {
        if (patientSelectionne == null) {
            AlertUtil.afficherErreur("Selectionnez d'abord un patient dans le tableau.");
            return;
        }

        try {
            Patient patient = construirePatientDepuisFormulaire();
            patientService.modifierPatient(patient);

            AlertUtil.afficherSucces("Patient modifie avec succes.");
            chargerPatients();
            onNouveau();

        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    // Supprime le patient actuellement selectionne dans le tableau
    @FXML
    private void onSupprimer() {
        if (patientSelectionne == null) {
            AlertUtil.afficherErreur("Selectionnez d'abord un patient dans le tableau.");
            return;
        }

        try {
            patientService.supprimerPatient(patientSelectionne.getId());

            AlertUtil.afficherSucces("Patient supprime avec succes.");
            chargerPatients();
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