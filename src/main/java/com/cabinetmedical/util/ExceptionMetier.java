package com.cabinetmedical.util;

// Exception métier dédiée aux erreurs de validation ou de logique métier.

public class ExceptionMetier extends Exception {

    private static final long serialVersionUID = 1L;

    public ExceptionMetier() {
        super();
    }

    public ExceptionMetier(String message) {
        super(message);
    }

    public ExceptionMetier(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionMetier(Throwable cause) {
        super(cause);
    }
}