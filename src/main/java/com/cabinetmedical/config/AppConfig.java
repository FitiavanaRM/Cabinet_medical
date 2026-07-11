package com.cabinetmedical.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

// lire le fichier config.xml
public class AppConfig {

    //private static final String path = "config.xml";

    private String host;
    private String port;
    private String dbName;
    private String username;
    private String password;
    private String params;

    private static AppConfig instance;

    public AppConfig() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/config.xml");
            if (inputStream == null) {
                throw new RuntimeException("Fichier config.xml introuvable!");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            // valeur correspondant
            host = getTag(document, "host");
            port = getTag(document, "port");
            dbName = getTag(document, "name");
            username = getTag(document, "username");
            password = getTag(document, "password");

        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new RuntimeException("Erreur lors de la lecture de config.xml : " + exception.getMessage(), exception);
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    // construit l'URL

    public String buildJdbcUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    }

    private String getTag(Document document, String tagName) {
        Element element = (Element) document.getElementsByTagName(tagName).item(0);
        if (element == null) {
            throw new RuntimeException("<" + tagName + "> manquante dans config.xml");
        }
        return element.getTextContent().trim();
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password != null ? password : "";
    }

    public String getDbName() {
        return dbName;
    }

    public String getUsername() {
        return username;
    }

    public String getParams() {
        return params;
    }

    public String getPort() {
        return port;
    }
}