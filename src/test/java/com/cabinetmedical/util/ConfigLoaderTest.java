package com.cabinetmedical.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    @Test
    void shouldLoadDatabaseConfigurationFromXml() {
        ConfigLoader config = ConfigLoader.getInstance();

        assertEquals("localhost", config.getHost());
        assertEquals("3306", config.getPort());
        assertEquals("cabinet_medical", config.getDbName());
        assertEquals("root", config.getUsername());
        assertEquals("", config.getPassword());
        assertTrue(config.buildJdbcUrl().contains("jdbc:mysql://localhost:3306/cabinet_medical"));
        assertTrue(config.buildJdbcUrl().contains("useSSL=false"));
    }
}
