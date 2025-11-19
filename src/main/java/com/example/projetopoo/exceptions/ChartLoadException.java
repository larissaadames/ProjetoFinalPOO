package com.example.projetopoo.exceptions;

public class ChartLoadException extends RuntimeException {

    public ChartLoadException(String message) {
        super(message);
    }

    public ChartLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
