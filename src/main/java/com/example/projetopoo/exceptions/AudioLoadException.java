package com.example.projetopoo.exceptions;

public class AudioLoadException extends RuntimeException {

    public AudioLoadException(String message) {
        super(message);
    }

    public AudioLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
