package com.cabinetmedical.util;

import com.cabinetmedical.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Connexion JDBC vers Mysql
public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        AppConfig config = AppConfig.getInstance();

        return DriverManager.getConnection(
                config.buildJdbcUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }
}