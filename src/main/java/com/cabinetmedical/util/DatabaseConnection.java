package com.cabinetmedical.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Connexion JDBC vers Mysql
public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        ConfigLoader config = ConfigLoader.getInstance();

        return DriverManager.getConnection(
                config.buildJdbcUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }
}