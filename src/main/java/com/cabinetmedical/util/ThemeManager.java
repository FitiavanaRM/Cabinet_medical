package com.cabinetmedical.util;

import javafx.scene.Scene;

import java.net.URL;

public class ThemeManager {

    public enum Theme {
        LIGHT("/com/cabinetmedical/css/style.css"),
        DARK("/com/cabinetmedical/css/style-dark.css");

        private final String cssPath;

        Theme(String cssPath) {
            this.cssPath = cssPath;
        }

        public String getCssPath() {
            return cssPath;
        }
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static boolean isDark() {
        return currentTheme == Theme.DARK;
    }

    public static void setTheme(Theme theme) {
        currentTheme = (theme == null) ? Theme.LIGHT : theme;
    }

    public static void toggle() {
        currentTheme = (currentTheme == Theme.DARK) ? Theme.LIGHT : Theme.DARK;
    }

    public static void applyToScene(Scene scene) {
        if (scene == null) {
            return;
        }

        scene.getStylesheets().removeIf(sheet ->
                sheet.endsWith("style.css") || sheet.endsWith("style-dark.css")
        );

        URL cssUrl = ThemeManager.class.getResource(currentTheme.getCssPath());
        if (cssUrl != null) {
            String css = cssUrl.toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
        }
    }
}
