package com.cabinetmedical.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ConfigLoader {

    private String host;
    private String port;
    private String dbName;
    private String username;
    private String password;
    private String params;

    private static ConfigLoader instance;

    private ConfigLoader() {
        loadConfig();
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }

    private void loadConfig() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/com/cabinetmedical/config/config.xml");
            if (inputStream == null) {
                throw new RuntimeException("Fichier config.xml introuvable!");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            Element database = getChildElement(document.getDocumentElement(), "database");
            host = getTag(database, "host");
            port = getTag(database, "port");
            dbName = getTag(database, "name");
            username = getTag(database, "username");
            password = getTag(database, "password");
            params = getTag(database, "params");

        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new RuntimeException("Erreur lors de la lecture de config.xml : " + exception.getMessage(), exception);
        }
    }

    public String buildJdbcUrl() {
        String baseUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        if (params != null && !params.isBlank()) {
            return baseUrl + "?" + params;
        }
        return baseUrl + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    }

    private Element getChildElement(Element parent, String tagName) {
        if (parent == null) {
            throw new RuntimeException("<database> manquante dans config.xml");
        }
        Element element = (Element) parent.getElementsByTagName(tagName).item(0);
        if (element == null) {
            throw new RuntimeException("<" + tagName + "> manquante dans config.xml");
        }
        return element;
    }

    private String getTag(Element parent, String tagName) {
        Element element = (Element) parent.getElementsByTagName(tagName).item(0);
        if (element == null) {
            throw new RuntimeException("<" + tagName + "> manquante dans config.xml");
        }
        return element.getTextContent().trim();
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password != null ? password : "";
    }

    public String getParams() {
        return params;
    }
}
