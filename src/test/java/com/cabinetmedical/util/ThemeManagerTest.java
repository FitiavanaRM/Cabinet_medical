package com.cabinetmedical.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThemeManagerTest {

    @Test
    void toggleSwitchesTheme() {
        ThemeManager.setTheme(ThemeManager.Theme.LIGHT);
        ThemeManager.toggle();

        assertEquals(ThemeManager.Theme.DARK, ThemeManager.getCurrentTheme());

        ThemeManager.setTheme(ThemeManager.Theme.LIGHT);
    }

    @Test
    void darkThemeUsesDarkCss() {
        ThemeManager.setTheme(ThemeManager.Theme.DARK);

        assertTrue(ThemeManager.Theme.DARK.getCssPath().endsWith("style-dark.css"));

        ThemeManager.setTheme(ThemeManager.Theme.LIGHT);
    }
}
