package com.cabinetmedical.dao;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.util.DatabaseConnection;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ConsultationDAO {
    public List<Consultation> findAll() throws Exception {
        List<Consultation> liste = new ArrayList<>();
        String sql = "SELECT * FROM consultation ORDER BY date_consultation";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Consultation consultation = new Consultation(
                        resultSet.getInt("id_consultation"),
                        resultSet.getInt("id_rdv"),
                        resultSet.getString("diagnostic"),
                        resultSet.getString("ordonnance"),
                        resultSet.getTimestamp("date_consultation").toLocalDateTime()
                );
                liste.add(consultation);
            }
        }
        return liste;
    }

    // recherche
    public Consultation findById(int id) throws Exception {
        String sql = "SELECT * FROM consultation WHERE id_consultation = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet =  preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Consultation consultation = new Consultation(
                            resultSet.getInt("id_consultation"),
                            resultSet.getInt("id_rdv"),
                            resultSet.getString("diagnostic"),
                            resultSet.getString("ordonnance"),
                            resultSet.getTimestamp("date_consultation").toLocalDateTime()
                    );
                    return consultation;
                }
            }
        }
        return null;
    }

    // insertion
    public void update(Consultation consultation) throws Exception {
        String sql = "UPDATE consultation SET diagnostic = ?, ordonnance = ? WHERE id_consultation = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, consultation.getDiagnostic());
            preparedStatement.setString(2, consultation.getOrdonnance());
            preparedStatement.setInt(3, consultation.getId());

            preparedStatement.executeUpdate();
        }
    }

    // suppression
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM consultation WHERE id_consultation = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // nombre total de consultation
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM consultation";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0;
    }

    // verification de l'existance du rendez vous du consultation
    public boolean existsForRdv(int idRdv) throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM consultation WHERE id_rdv = ?";

         try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idRdv);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total") > 0;
                }
            }
        }
        return false;
    }

    // recupere la consultation liee au rdv
    public Consultation findByRdv(int idRdv) throws Exception {
        String sql = "SELECT * FROM consultation WHERE id_rdv = ?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idRdv);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Consultation c = new Consultation(
                            resultSet.getInt("id_consultation"),
                            resultSet.getInt("id_rdv"),
                            resultSet.getString("diagnostic"),
                            resultSet.getString("ordonnance"),
                            resultSet.getTimestamp("date_consultation").toLocalDateTime()
                    );
                    return c;
                }
            }
        }

        return null;
    }
}

