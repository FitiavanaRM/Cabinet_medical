package com.cabinetmedical.service;

import com.cabinetmedical.dao.PatientDAO;
import com.cabinetmedical.dao.MedecinDAO;
import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.dao.ConsultationDAO;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.StatutRendezVous;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Raport et statistique du cabinet
public class RapportService {
    private PatientDAO patientDAO = new PatientDAO();
    private MedecinDAO medecinDAO = new MedecinDAO();
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();
    private ConsultationDAO consultationDAO = new ConsultationDAO();

    // effectif general, avec les nombres de patients, medecins, rdv, consultation
    public Map<String, Integer> statistiquesGenerales() throws Exception {
        Map<String, Integer> statistique = new LinkedHashMap<>();
        statistique.put("Patients", patientDAO.count());
        statistique.put("Medecins", medecinDAO.count());
        statistique.put("Rendez-vous", rendezVousDAO.count());
        statistique.put("Consultations", consultationDAO.count());
        return statistique;
    }

    // nombre de rendez-vous pour chaque statut
    public Map<StatutRendezVous, Integer> rendezVousParStatut() throws Exception {
        Map<StatutRendezVous, Integer> repartition = new LinkedHashMap<>();

        for (StatutRendezVous statut : StatutRendezVous.values()) {
            repartition.put(statut, 0);
        }

        List<RendezVous> tousLesRdv = rendezVousDAO.findAll();
        for (RendezVous rdv : tousLesRdv) {
            StatutRendezVous statut = rdv.getStatut();
            repartition.put(statut, repartition.get(statut) + 1);
        }
        return repartition;
    }

    // nombre de rendez-vous pour chaque medecin
    public Map<String, Integer> rendezVousParMedecin() throws Exception {
        Map<String, Integer> repartition = new LinkedHashMap<>();

        List<Medecin> medecins = medecinDAO.findAll();
        for (Medecin medecin : medecins) {
            String nomComplet = medecin.getPrenom() + " " + medecin.getNom();
            int nombreRdv = rendezVousDAO.findByMedecin(medecin.getId()).size();
            repartition.put(nomComplet, nombreRdv);
        }
        return repartition;
    }

    // nombre de consultations par chaque medecin
    public Map<String, Integer> consultationsParMedecin() throws Exception {
        Map<String, Integer> repartition = new LinkedHashMap<>();

        List<Medecin> medecins = medecinDAO.findAll();
        for (Medecin medecin : medecins) {
            String nomComplet = medecin.getPrenom() + " " + medecin.getNom();
            int nombreConsultations = 0;

            // On parcourt les rendez-vous HONORE du medecin et on verifie les consultations de chacun
            List<RendezVous> rdvMedecin = rendezVousDAO.findByMedecin(medecin.getId());
            for (RendezVous rdv : rdvMedecin) {
                if (rdv.getStatut() == StatutRendezVous.HONORE
                        && consultationDAO.existsForRdv(rdv.getId())) {
                    nombreConsultations++;
                }
            }
            repartition.put(nomComplet, nombreConsultations);
        }
        return repartition;
    }

    // patients ayant eu le plus de rendez-vous,
    public List<String> topPatientsAssidus(int limite) throws Exception {
        // calcule le nombre de rendez-vous de chaque patient
        Map<Patient, Integer> nombreRdvParPatient = new LinkedHashMap<>();
        List<Patient> patients = patientDAO.findAll();

        for (Patient patient : patients) {
            int nombreRdv = rendezVousDAO.findByPatient(patient.getId()).size();
            nombreRdvParPatient.put(patient, nombreRdv);
        }

        // trie les patients par nombre de rendez-vous decroissant
        List<Map.Entry<Patient, Integer>> entrees = new ArrayList<>(nombreRdvParPatient.entrySet());
        entrees.sort((entree1, entree2) -> entree2.getValue() - entree1.getValue());

        // construit la liste finale
        List<String> resultat = new ArrayList<>();
        for (int i = 0; i < entrees.size() && i < limite; i++) {
            Map.Entry<Patient, Integer> entree = entrees.get(i);
            Patient patient = entree.getKey();
            int nombreRdv = entree.getValue();

            resultat.add(patient.getPrenom() + " " + patient.getNom() + " : " + nombreRdv + " rendez-vous");
        }
        return resultat;
    }
}