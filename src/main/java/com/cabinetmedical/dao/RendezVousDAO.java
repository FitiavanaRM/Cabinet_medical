package com.cabinetmedical.dao;

import com.cabinetmedical.util.DatabaseConnection;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.StatutRendezVous;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.Time;

public class RendezVousDAO {
    public List<RendezVous> findAll() throws Exception {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous ORDER BY date_rdv, heure_rdv";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                RendezVous r = new RendezVous(
                        resultSet.getInt("id_rdv"),
                        resultSet.getInt("id_patient"),
                        resultSet.getInt("id_medecin"),
                        resultSet.getDate("date_rdv").toLocalDate(),
                        resultSet.getTime("heure_rdv").toLocalTime(),
                        resultSet.getString("motif"),
                        StatutRendezVous.valueOf(resultSet.getString("statut"))
                );
                liste.add(r);
            }
        }
        return liste;
    }

    // Recherche
    public RendezVous findById(int id) throws Exception {
        String sql = "SELECT * FROM rendez_vous WHERE id_rdv = ?";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RendezVous rendezVous = new RendezVous(
                            resultSet.getInt("id_rdv"),
                            resultSet.getInt("id_patient"),
                            resultSet.getInt("id_medecin"),
                            resultSet.getDate("date_rdv").toLocalDate(),
                            resultSet.getTime("heure_rdv").toLocalTime(),
                            resultSet.getString("motif"),
                            StatutRendezVous.valueOf(resultSet.getString("statut"))
                    );
                    return rendezVous;
                }
            }
        }
        return null;
    }

    // Insertion
    public void insert(RendezVous rdv) throws Exception {
        String sql = "INSERT INTO rendez_vous (id_patient, id_medecin, date_rdv, heure_rdv, motif, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, rdv.getIdPatient());
            preparedStatement.setInt(2, rdv.getIdMedecin());
            preparedStatement.setDate(3, Date.valueOf(rdv.getDateRdv()));
            preparedStatement.setTime(4, Time.valueOf(rdv.getHeureRdv()));
            preparedStatement.setString(5, rdv.getMotif());
            preparedStatement.setString(6, rdv.getStatut().name());

            preparedStatement.executeUpdate();
        }
    }

    // Mise à jour
    public void update(RendezVous rdv) throws Exception {
        String sql = "UPDATE rendez_vous SET id_patient = ?, id_medecin = ?, date_rdv = ?, heure_rdv = ?, motif = ?, statut = ? WHERE id_rdv = ?";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, rdv.getIdPatient());
            statement.setInt(2, rdv.getIdMedecin());
            statement.setDate(3, Date.valueOf(rdv.getDateRdv()));
            statement.setTime(4, Time.valueOf(rdv.getHeureRdv()));
            statement.setString(5, rdv.getMotif());
            statement.setString(6, rdv.getStatut().name());
            statement.setInt(7, rdv.getId());

            statement.executeUpdate();
        }
    }

    // Suppression
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM rendez_vous WHERE id_rdv = ?";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // nombre total de rendez-vous
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM rendez_vous";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0;
    }

    // Recupere l'historique des rendez-vous d patient
    public List<RendezVous> findByPatient(int idPatient) throws Exception {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE id_patient = ? ORDER BY date_rdv, heure_rdv";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idPatient);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RendezVous rendezVous = new RendezVous(
                            resultSet.getInt("id_rdv"),
                            resultSet.getInt("id_patient"),
                            resultSet.getInt("id_medecin"),
                            resultSet.getDate("date_rdv").toLocalDate(),
                            resultSet.getTime("heure_rdv").toLocalTime(),
                            resultSet.getString("motif"),
                            StatutRendezVous.valueOf(resultSet.getString("statut"))
                    );
                    liste.add(rendezVous);
                }
            }
        }
        return liste;
    }

    // Recupere le planning des rendez-vous d'un medecin
    public List<RendezVous> findByMedecin(int idMedecin) throws Exception {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE id_medecin = ? ORDER BY date_rdv, heure_rdv";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idMedecin);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    RendezVous rendezVous = new RendezVous(
                            resultSet.getInt("id_rdv"),
                            resultSet.getInt("id_patient"),
                            resultSet.getInt("id_medecin"),
                            resultSet.getDate("date_rdv").toLocalDate(),
                            resultSet.getTime("heure_rdv").toLocalTime(),
                            resultSet.getString("motif"),
                            StatutRendezVous.valueOf(resultSet.getString("statut"))
                    );
                    liste.add(rendezVous);
                }
            }
        }
        return liste;
    }

    // verifivation dispo d medecin
    public boolean existeChevauchement(int idMedecin, java.time.LocalDate date, java.time.LocalTime heure) throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM rendez_vous WHERE id_medecin = ? AND date_rdv = ? AND heure_rdv = ? AND statut != 'ANNULE'";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idMedecin);
            statement.setDate(2, Date.valueOf(date));
            statement.setTime(3, Time.valueOf(heure));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total") > 0;
                }
            }
        }
        return false;
    }
}