package com.cabinetmedical.dao;

import com.cabinetmedical.util.DatabaseConnection;
import com.cabinetmedical.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PatientDAO {
    public List<Patient> findAll() throws Exception {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT * FROM patient ORDER BY nom, prenom";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Patient p = new Patient(
                        resultSet.getInt("id_patient"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getDate("date_naissance").toLocalDate(),
                        resultSet.getString("telephone"),
                        resultSet.getString("adresse"),
                        resultSet.getString("antecedents")
                );
                liste.add(p);
            }
        }
        return liste;
    }

    public Patient findById(int id) throws Exception {
        String sql = "SELECT * FROM patient WHERE id_patient = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Patient p = new Patient(
                            resultSet.getInt("id_patient"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getDate("date_naissance").toLocalDate(),
                            resultSet.getString("telephone"),
                            resultSet.getString("adresse"),
                            resultSet.getString("antecedents")
                    );
                    return p;
                }
            }
        }
        return null;
    }

    // Insert un patient dans la base
    public void insert(Patient patient) throws Exception {
        String sql = "INSERT INTO patient (nom, prenom, date_naissance, telephone, adresse, antecedents) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getNom());
            preparedStatement.setString(2, patient.getPrenom());
            preparedStatement.setString(3, String.valueOf(Date.valueOf(patient.getDateNaissance())));
            preparedStatement.setString(4, patient.getTelephone());
            preparedStatement.setString(5, patient.getAdresse());
            preparedStatement.setString(6, patient.getAntecedents());

            preparedStatement.executeUpdate();
        }
    }

    // Mise à jour
    public void update(Patient patient) throws Exception {
        String sql = "UPDATE patient SET nom = ?, prenom = ?, date_naissance = ?, telephone = ?, adresse = ?, antecedents = ? WHERE id_patient = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {

            preparedStatement.setString(1, patient.getNom());
            preparedStatement.setString(2, patient.getPrenom());
            preparedStatement.setDate(3, Date.valueOf(patient.getDateNaissance()));
            preparedStatement.setString(4, patient.getTelephone());
            preparedStatement.setString(5, patient.getAdresse());
            preparedStatement.setString(6, patient.getAntecedents());
            preparedStatement.setInt(7, patient.getId());

            preparedStatement.executeUpdate();
        }
    }

    // suppressio,n
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM patient WHERE id_patient = ?";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // nombre total de patients
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM patient";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0;
    }
}