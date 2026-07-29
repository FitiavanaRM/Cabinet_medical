package com.cabinetmedical.dao;

import com.cabinetmedical.util.DatabaseConnection;
import com.cabinetmedical.model.Medecin;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Time;

public class MedecinDAO {

    public List<Medecin> findAll() throws Exception {
        List<Medecin> liste = new ArrayList<>();
        String sql = "SELECT * FROM medecin ORDER BY nom, prenom";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Medecin m = new Medecin(
                        resultSet.getInt("id_medecin"),
                        resultSet.getObject("id_utilisateur", Integer.class),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getString("jours_disponibles"),
                        resultSet.getTime("heure_debut_dispo").toLocalTime(),
                        resultSet.getTime("heure_fin_dispo").toLocalTime()
                );
                liste.add(m);
            }
        }
        return liste;
    }

    // Recherche
    public Medecin findById(int id) throws Exception {
        String sql = "SELECT * FROM medecin WHERE id_medecin = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Medecin medecin = new Medecin(
                            resultSet.getInt("id_medecin"),
                            resultSet.getObject("id_utilisateur", Integer.class),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("specialite"),
                            resultSet.getString("telephone"),
                            resultSet.getString("email"),
                            resultSet.getString("jours_disponibles"),
                            resultSet.getTime("heure_debut_dispo").toLocalTime(),
                            resultSet.getTime("heure_fin_dispo").toLocalTime()
                    );
                    return medecin;
                }
            }
        }
        return null;
    }

    // Insertion
    public void insert(Medecin medecin) throws Exception {
        String sql = "INSERT INTO medecin (id_utilisateur, nom, prenom, specialite, telephone, email, jours_disponibles, heure_debut_dispo, heure_fin_dispo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, medecin.getIdUtilisateur());
            preparedStatement.setString(2, medecin.getNom());
            preparedStatement.setString(3, medecin.getPrenom());
            preparedStatement.setString(4, medecin.getSpecialite());
            preparedStatement.setString(5, medecin.getTelephone());
            preparedStatement.setString(6, medecin.getEmail());
            preparedStatement.setString(7, medecin.getJoursDisponibles());
            preparedStatement.setTime(8, Time.valueOf(medecin.getHeureDebutDispo()));
            preparedStatement.setTime(9, Time.valueOf(medecin.getHeureFinDispo()));

            preparedStatement.executeUpdate();
        }
    }

    // Met a jour un medecin existant, identifie par son id
    public void update(Medecin medecin) throws Exception {
        String sql = "UPDATE medecin SET id_utilisateur = ?, nom = ?, prenom = ?, specialite = ?, " +
                     "telephone = ?, email = ?, jours_disponibles = ?, heure_debut_dispo = ?, heure_fin_dispo = ? " +
                     "WHERE id_medecin = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, medecin.getIdUtilisateur());
            preparedStatement.setString(2, medecin.getNom());
            preparedStatement.setString(3, medecin.getPrenom());
            preparedStatement.setString(4, medecin.getSpecialite());
            preparedStatement.setString(5, medecin.getTelephone());
            preparedStatement.setString(6, medecin.getEmail());
            preparedStatement.setString(7, medecin.getJoursDisponibles());
            preparedStatement.setTime(8, Time.valueOf(medecin.getHeureDebutDispo()));
            preparedStatement.setTime(9, Time.valueOf(medecin.getHeureFinDispo()));
            preparedStatement.setInt(10, medecin.getId());

            preparedStatement.executeUpdate();
        }
    }

    // Suppression
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM medecin WHERE id_medecin = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // nombre total de medecins
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM medecin";

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
