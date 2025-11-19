package com.example.projetopoo;

import javafx.scene.text.Font;

public class ErroLoadFonteException extends RuntimeException {

    public ErroLoadFonteException(String mensagemErro) {
        super(mensagemErro + "\n\n" +
                "FONTES DISPONÍVEIS (NOME INTERNO):\n" +
                String.join(", ", Font.getFontNames()));
    }

    public ErroLoadFonteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}