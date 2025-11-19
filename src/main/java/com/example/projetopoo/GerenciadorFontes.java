package com.example.projetopoo;

import javafx.scene.text.Font;
import java.io.InputStream;

public class GerenciadorFontes {

    public static String carregar(String caminhoRelativo) {

        try (InputStream is = GerenciadorFontes.class.getResourceAsStream(caminhoRelativo)) {

            if (is == null) {
                throw new ErroLoadFonteException("Arquivo de fonte não encontrado. Caminho: " + caminhoRelativo);
            }

            Font fontCarregada = Font.loadFont(is, 10);

            if (fontCarregada == null) {
                throw new ErroLoadFonteException("Falha ao carregar a fonte. O arquivo pode estar corrompido ou o formato não é suportado: " + caminhoRelativo);
            }

            String nomeInterno = fontCarregada.getName();
            System.out.println("FONTE CARREGADA: '" + caminhoRelativo + "'. Nome interno: " + nomeInterno);

            return nomeInterno;

        } catch (ErroLoadFonteException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroLoadFonteException("Erro inesperado ao carregar fonte.", e);
        }
    }
}