package com.example.projetopoo.exceptions;

import javafx.scene.text.Font;

public class ErroLoadFonteException extends RuntimeException {

    public ErroLoadFonteException(String mensagemErro) {
        super(mensagemErro);
    }

    public ErroLoadFonteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}