package com.cabinetmedical.dao;

import com.cabinetmedical.util.DatabaseConnection;
import com.cabinetmedical.model.Utilisateur;
import com.cabinetmedical.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class UtilisateurDAO {

    // Recupere tous les utilisateurs
    public List<Utilisateur> findAll() throws Exception {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY login";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Utilisateur u = new Utilisateur(
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getString("login"),
                        resultSet.getString("mot_de_passe"),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getTimestamp("date_creation").toLocalDateTime()
                );
                liste.add(u);
            }
        }
        return liste;
    }

    // Recherche
    public Utilisateur findById(int id) throws Exception {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Utilisateur utilisateur = new Utilisateur(
                            resultSet.getInt("id_utilisateur"),
                            resultSet.getString("login"),
                            resultSet.getString("mot_de_passe"),
                            Role.valueOf(resultSet.getString("role")),
                            resultSet.getTimestamp("date_creation").toLocalDateTime()
                    );
                    return utilisateur;
                }
            }
        }
        return null;
    }

    // Insertion
    public void insert(Utilisateur utilisateur) throws Exception {
        String sql = "INSERT INTO utilisateur (login, mot_de_passe, role) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, utilisateur.getLogin());
            preparedStatement.setString(2, utilisateur.getMotDePasse());
            preparedStatement.setString(3, utilisateur.getRole().name());
            preparedStatement.executeUpdate();
        }
    }

    // Mise à jour
    public void update(Utilisateur utilisateur) throws Exception {
        String sql = "UPDATE utilisateur SET login = ?, mot_de_passe = ?, role = ? WHERE id_utilisateur = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, utilisateur.getLogin());
            preparedStatement.setString(2, utilisateur.getMotDePasse());
            preparedStatement.setString(3, utilisateur.getRole().name());
            preparedStatement.setInt(4, utilisateur.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Suppression
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // nombre total d'utilisateurs
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM utilisateur";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0;
    }

    // recherche à partir du login
    public Utilisateur findByLogin(String login) throws Exception {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Utilisateur utilisateur = new Utilisateur(
                            resultSet.getInt("id_utilisateur"),
                            resultSet.getString("login"),
                            resultSet.getString("mot_de_passe"),
                            Role.valueOf(resultSet.getString("role")),
                            resultSet.getTimestamp("date_creation").toLocalDateTime()
                    );
                    return utilisateur;
                }
            }
        }
        return null;
    }
}