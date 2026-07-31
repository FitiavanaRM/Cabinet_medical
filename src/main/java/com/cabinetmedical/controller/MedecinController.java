package com.cabinetmedical.controller;

import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.service.MedecinService;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

// control d'ecran du medecin
public class MedecinController implements Initializable {

    @FXML private TableView<Medecin> tableMedecins;
    @FXML private TableColumn<Medecin, Integer> colId;
    @FXML private TableColumn<Medecin, String> colNom;
    @FXML private TableColumn<Medecin, String> colPrenom;
    @FXML private TableColumn<Medecin, String> colSpecialite;
    @FXML private TableColumn<Medecin, String> colTelephone;
    @FXML private TableColumn<Medecin, String> colEmail;

    @FXML private TextField champNom;
    @FXML private TextField champPrenom;
    @FXML private TextField champSpecialite;
    @FXML private TextField champTelephone;
    @FXML private TextField champEmail;
    @FXML private TextField champJoursDisponibles;
    @FXML private TextField champHeureDebut;
    @FXML private TextField champHeureFin;
    @FXML private javafx.scene.layout.StackPane rootPane;

    private MedecinService medecinService = new MedecinService();
    private ObservableList<Medecin> listeMedecins = FXCollections.observableArrayList();
    private Medecin medecinSelectionne;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableMedecins.setItems(listeMedecins);

        tableMedecins.getSelectionModel().selectedItemProperty().addListener((observable, ancien, nouveau) -> {
            if (nouveau != null) {
                remplirFormulaire(nouveau);
            }
        });

        chargerMedecins();
    }

    // Recupere tous les medecins depuis MedecinService
    private void chargerMedecins() {
        try {
            listeMedecins.setAll(medecinService.listerTousMedecins());
        } catch (Exception e) {
            Toast.error(rootPane, "Impossible de charger les médecins : " + e.getMessage());
        }
    }

    // Remplit les champs du formulaire
    private void remplirFormulaire(Medecin medecin) {
        medecinSelectionne = medecin;
        champNom.setText(medecin.getNom());
        champPrenom.setText(medecin.getPrenom());
        champSpecialite.setText(medecin.getSpecialite());
        champTelephone.setText(medecin.getTelephone());
        champEmail.setText(medecin.getEmail());
        champJoursDisponibles.setText(medecin.getJoursDisponibles());
        champHeureDebut.setText(medecin.getHeureDebutDispo().toString());
        champHeureFin.setText(medecin.getHeureFinDispo().toString());
    }

    // Vide le formulaire et deselectionne le tableau
    @FXML
    private void onNouveau() {
        medecinSelectionne = null;
        champNom.clear();
        champPrenom.clear();
        champSpecialite.clear();
        champTelephone.clear();
        champEmail.clear();
        champJoursDisponibles.clear();
        champHeureDebut.clear();
        champHeureFin.clear();
        tableMedecins.getSelectionModel().clearSelection();
    }

    private Medecin construireMedecinDepuisFormulaire() {
        LocalTime heureDebut;
        LocalTime heureFin;

        // On verifie le format des heures ici
        try {
            heureDebut = LocalTime.parse(champHeureDebut.getText());
            heureFin = LocalTime.parse(champHeureFin.getText());
        } catch (DateTimeParseException e) {
            Toast.error(rootPane, "Format d'heure invalide. Utilisez le format HH:mm (ex: 08:00).");
            return null;
        }

        Medecin medecin = new Medecin();

        if (medecinSelectionne != null) {
            medecin.setId(medecinSelectionne.getId());
            medecin.setIdUtilisateur(medecinSelectionne.getIdUtilisateur());
        }

        medecin.setNom(champNom.getText());
        medecin.setPrenom(champPrenom.getText());
        medecin.setSpecialite(champSpecialite.getText());
        medecin.setTelephone(champTelephone.getText());
        medecin.setEmail(champEmail.getText());
        medecin.setJoursDisponibles(champJoursDisponibles.getText());
        medecin.setHeureDebutDispo(heureDebut);
        medecin.setHeureFinDispo(heureFin);

        return medecin;
    }

    // Ajoute un nouveau medecin
    @FXML
    private void onAjouter() {
        Medecin medecin = construireMedecinDepuisFormulaire();
        if (medecin == null) {
            Toast.error(rootPane, "Format d'heure invalide. Utilisez HH:mm.");
            return;
        }

        try {
            medecinService.ajouterMedecin(medecin);

            Toast.success(rootPane, "Médecin ajouté avec succès.");
            chargerMedecins();
            onNouveau();

        } catch (Exception e) {
            Toast.error(rootPane, e.getMessage());
        }
    }

    // Modifie le medecin
    @FXML
    private void onModifier() {
        if (medecinSelectionne == null) {
            Toast.error(rootPane, "Sélectionnez d'abord un médecin dans le tableau.");
            return;
        }

        Medecin medecin = construireMedecinDepuisFormulaire();
        if (medecin == null) {
            Toast.error(rootPane, "Format d'heure invalide. Utilisez HH:mm.");
            return;
        }

        try {
            medecinService.modifierMedecin(medecin);
            Toast.success(rootPane, "Médecin modifié avec succès.");
            chargerMedecins();
            onNouveau();

        } catch (Exception e) {
            Toast.error(rootPane, e.getMessage());
        }
    }

    // Supprime le medecin
    @FXML
    private void onSupprimer() {
        if (medecinSelectionne == null) {
            Toast.error(rootPane, "Sélectionnez d'abord un médecin dans le tableau.");
            return;
        }

        try {
            medecinService.supprimerMedecin(medecinSelectionne.getId());

            Toast.success(rootPane, "Médecin supprimé avec succès.");
            chargerMedecins();
            onNouveau();

        } catch (Exception e) {
            Toast.error(rootPane, e.getMessage());
        }
    }

    // Revient a l'ecran principal
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