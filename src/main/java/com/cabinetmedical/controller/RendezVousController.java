package com.cabinetmedical.controller;

import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.service.RendezVousService;
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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

// controle l'ecran des rendez-vous
public class RendezVousController implements Initializable {

    @FXML private TableView<RendezVous> tableRendezVous;
    @FXML private TableColumn<RendezVous, Integer> colId;
    @FXML private TableColumn<RendezVous, Integer> colPatient;
    @FXML private TableColumn<RendezVous, Integer> colMedecin;
    @FXML private TableColumn<RendezVous, Object> colDate;
    @FXML private TableColumn<RendezVous, Object> colHeure;
    @FXML private TableColumn<RendezVous, String> colStatut;

    @FXML private TextField champPatient;
    @FXML private TextField champMedecin;
    @FXML private DatePicker champDate;
    @FXML private TextField champHeure;
    @FXML private TextArea champMotif;

    private RendezVousService rendezVousService = new RendezVousService();
    private ObservableList<RendezVous> listeRendezVous = FXCollections.observableArrayList();
    private RendezVous rendezVousSelectionne;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("idMedecin"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateRdv"));
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heureRdv"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        tableRendezVous.setItems(listeRendezVous);

        tableRendezVous.getSelectionModel().selectedItemProperty().addListener((observable, ancien, nouveau) -> {
            if (nouveau != null) {
                remplirFormulaire(nouveau);
            }
        });

        chargerRendezVous();
    }

    private void chargerRendezVous() {
        try {
            listeRendezVous.setAll(rendezVousService.listeRdv());
        } catch (Exception e) {
            AlertUtil.afficherErreur("Impossible de charger les rendez-vous : " + e.getMessage());
        }
    }

    private void remplirFormulaire(RendezVous rdv) {
        rendezVousSelectionne = rdv;
        champPatient.setText(String.valueOf(rdv.getIdPatient()));
        champMedecin.setText(String.valueOf(rdv.getIdMedecin()));
        champDate.setValue(rdv.getDateRdv());
        champHeure.setText(rdv.getHeureRdv().toString());
        champMotif.setText(rdv.getMotif());
    }

    @FXML
    private void onNouveau() {
        rendezVousSelectionne = null;
        champPatient.clear();
        champMedecin.clear();
        champDate.setValue(null);
        champHeure.clear();
        champMotif.clear();
        tableRendezVous.getSelectionModel().clearSelection();
    }

    private RendezVous construireRendezVousDepuisFormulaire() {
        LocalTime heure;
        try {
            heure = LocalTime.parse(champHeure.getText());
        } catch (DateTimeParseException e) {
            AlertUtil.afficherErreur("Format d'heure invalide. Utilisez HH:mm, par exemple 15:00.");
            return null;
        }

        RendezVous rdv = new RendezVous();
        if (rendezVousSelectionne != null) {
            rdv.setId(rendezVousSelectionne.getId());
        }

        try {
            rdv.setIdPatient(Integer.parseInt(champPatient.getText()));
            rdv.setIdMedecin(Integer.parseInt(champMedecin.getText()));
        } catch (NumberFormatException e) {
            AlertUtil.afficherErreur("Les identifiants patient et medecin doivent etre des nombres entiers.");
            return null;
        }

        rdv.setDateRdv(champDate.getValue());
        rdv.setHeureRdv(heure);
        rdv.setMotif(champMotif.getText());
        return rdv;
    }

    @FXML
    private void onAjouter() {
        RendezVous rdv = construireRendezVousDepuisFormulaire();
        if (rdv == null) {
            return;
        }

        try {
            rendezVousService.creerRdv(rdv);
            AlertUtil.afficherSucces("Rendez-vous ajoute avec succes.");
            chargerRendezVous();
            onNouveau();
        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void onModifier() {
        if (rendezVousSelectionne == null) {
            AlertUtil.afficherErreur("Selectionnez d'abord un rendez-vous dans le tableau.");
            return;
        }

        RendezVous rdv = construireRendezVousDepuisFormulaire();
        if (rdv == null) {
            return;
        }

        try {
            rendezVousService.modifRdv(rdv);
            AlertUtil.afficherSucces("Rendez-vous modifie avec succes.");
            chargerRendezVous();
            onNouveau();
        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void onSupprimer() {
        if (rendezVousSelectionne == null) {
            AlertUtil.afficherErreur("Selectionnez d'abord un rendez-vous dans le tableau.");
            return;
        }

        try {
            rendezVousService.supprimerRendezVous(rendezVousSelectionne.getId());
            AlertUtil.afficherSucces("Rendez-vous supprime avec succes.");
            chargerRendezVous();
            onNouveau();
        } catch (Exception e) {
            AlertUtil.afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void onRetour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/main.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(racine));
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }
}