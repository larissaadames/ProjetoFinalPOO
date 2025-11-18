package com.example.projetopoo.exceptions;

public class SceneLoadException extends RuntimeException {

    public SceneLoadException(String message) {
        super(message);
    }

    public SceneLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
