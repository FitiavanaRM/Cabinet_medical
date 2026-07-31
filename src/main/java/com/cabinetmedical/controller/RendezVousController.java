package com.cabinetmedical.controller;

import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.service.RendezVousService;
import com.cabinetmedical.util.ThemeManager;
import com.cabinetmedical.util.Toast;

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
    @FXML private javafx.scene.layout.StackPane rootPane;

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
            Toast.error(rootPane, "Impossible de charger les rendez-vous : " + e.getMessage());
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
            Toast.error(rootPane, "Format d'heure invalide. Utilisez HH:mm, par exemple 15:00.");
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
            Toast.error(rootPane, "Les identifiants patient et médecin doivent être des nombres entiers.");
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
            Toast.error(rootPane, "Format invalide ou identifiants incorrects.");
            return;
        }

        try {
            rendezVousService.creerRdv(rdv);
            Toast.success(rootPane, "Rendez-vous ajouté avec succès.");
            chargerRendezVous();
            onNouveau();
        } catch (Exception e) {
            Toast.error(rootPane, e.getMessage());
        }
    }

    @FXML
    private void onModifier() {
        if (rendezVousSelectionne == null) {
            Toast.error(rootPane, "Sélectionnez d'abord un rendez-vous dans le tableau.");
            return;
        }

        RendezVous rdv = construireRendezVousDepuisFormulaire();
        if (rdv == null) {
            Toast.error(rootPane, "Format invalide ou identifiants incorrects.");
            return;
        }

        try {
            rendezVousService.modifRdv(rdv);
            Toast.success(rootPane, "Rendez-vous modifié avec succès.");
            chargerRendezVous();
            onNouveau();
        } catch (Exception e) {
            Toast.error(rootPane, e.getMessage());
        }
    }

    @FXML
    private void onSupprimer() {
        if (rendezVousSelectionne == null) {
            Toast.error(rootPane, "Sélectionnez d'abord un rendez-vous dans le tableau.");
            return;
        }

        try {
            rendezVousService.supprimerRendezVous(rendezVousSelectionne.getId());
            Toast.success(rootPane, "Rendez-vous supprimé avec succès.");
            chargerRendezVous();
            onNouveau();
        } catch (Exception e) {
            Toast.error(rootPane, e.getMessage());
        }
    }

    @FXML
    private void onRetour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cabinetmedical/fxml/main.fxml"));
        Parent racine = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(racine);
        ThemeManager.applyToScene(scene);
        stage.setScene(scene);
        stage.setTitle("Systeme de Gestion de Cabinet Medical");
    }
}