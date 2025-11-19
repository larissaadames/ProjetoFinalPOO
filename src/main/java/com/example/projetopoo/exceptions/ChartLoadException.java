package com.example.projetopoo.exceptions;

import javafx.scene.text.Font;

public class ChartLoadException extends RuntimeException {

    public ChartLoadException(String message) {
        super(message);
    }

    public ChartLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
