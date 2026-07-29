package com.cabinetmedical.controller;

import com.cabinetmedical.model.StatutRendezVous;
import com.cabinetmedical.service.RapportService;
import com.cabinetmedical.util.AlertUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

// controle l'ecran des rapports et statistiques
public class RapportController implements Initializable {

    @FXML private TextArea zoneStatistiquesGenerales;
    @FXML private TextArea zoneRdvParStatut;
    @FXML private TextArea zoneRdvParMedecin;
    @FXML private TextArea zoneTopPatients;

    private RapportService rapportService = new RapportService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chargerRapports();
    }

    // Charge tous les rapports calcules par RapportService
    private void chargerRapports() {
        try {
            afficherStatistiquesGenerales(rapportService.statistiquesGenerales());
            afficherRendezVousParStatut(rapportService.rendezVousParStatut());
            afficherRendezVousParMedecin(rapportService.rendezVousParMedecin());
            afficherTopPatients(rapportService.topPatientsAssidus(5));
        } catch (Exception e) {
            AlertUtil.afficherErreur("Impossible de charger les rapports : " + e.getMessage());
        }
    }

    // Affiche les effectifs generaux du cabinet
    private void afficherStatistiquesGenerales(Map<String, Integer> statistiques) {
        zoneStatistiquesGenerales.setText(construireTexte(statistiques));
    }

    // Affiche la repartition des rendez-vous selon leur statut
    private void afficherRendezVousParStatut(Map<StatutRendezVous, Integer> repartition) {
        zoneRdvParStatut.setText(construireTexte(repartition));
    }

    // Affiche le nombre de rendez-vous de chaque medecin
    private void afficherRendezVousParMedecin(Map<String, Integer> repartition) {
        zoneRdvParMedecin.setText(construireTexte(repartition));
    }

    // Affiche les patients les plus assidus
    private void afficherTopPatients(List<String> patients) {
        StringBuilder texte = new StringBuilder();
        for (String patient : patients) {
            texte.append(patient).append("\n");
        }
        zoneTopPatients.setText(texte.toString());
    }

    // Transforme une map de statistiques en texte lisible
    private String construireTexte(Map<?, Integer> donnees) {
        StringBuilder texte = new StringBuilder();
        for (Map.Entry<?, Integer> entree : donnees.entrySet()) {
            texte.append(entree.getKey()).append(" : ").append(entree.getValue()).append("\n");
        }
        return texte.toString();
    }

    // Actualise les donnees des rapports
    @FXML
    private void onActualiser() {
        chargerRapports();
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
