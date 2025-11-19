package com.example.projetopoo;

public class ConfigInitializer {

    // VARIÁVEL ESTÁTICA FINAL: Armazena o nome real da fonte.
    public static final String NOME_FONTE_8BIT;

    // ESSE STATIC garante que a fonte seja carregada ANTES da classe Main, que é importante
    static {
        try {
            NOME_FONTE_8BIT = GerenciadorFontes.carregar("/Fonts/8-bit-pusab.ttf");
        } catch (ErroLoadFonteException e) {
            e.printStackTrace();
            throw e;
        }
    }
}