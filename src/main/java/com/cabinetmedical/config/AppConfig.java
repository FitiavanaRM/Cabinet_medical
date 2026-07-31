package com.cabinetmedical.config;

import com.cabinetmedical.util.ConfigLoader;

public class AppConfig {

    private static final ConfigLoader delegate = ConfigLoader.getInstance();

    public AppConfig() {
    }

    public static AppConfig getInstance() {
        return new AppConfig();
    }

    public String buildJdbcUrl() {
        return delegate.buildJdbcUrl();
    }

    public String getHost() {
        return delegate.getHost();
    }

    public String getPassword() {
        return delegate.getPassword();
    }

    public String getDbName() {
        return delegate.getDbName();
    }

    public String getUsername() {
        return delegate.getUsername();
    }

    public String getParams() {
        return delegate.getParams();
    }

    public String getPort() {
        return delegate.getPort();
    }
}
