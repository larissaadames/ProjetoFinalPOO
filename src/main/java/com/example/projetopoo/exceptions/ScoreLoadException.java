package com.example.projetopoo.exceptions;

public class ScoreLoadException extends RuntimeException {

    public ScoreLoadException(String message) {
        super(message);
    }

    public ScoreLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
