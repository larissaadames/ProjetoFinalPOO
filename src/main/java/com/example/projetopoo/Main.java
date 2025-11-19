package com.example.projetopoo;

import com.example.projetopoo.jogo.core.JogoEngine;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String NOME_FONTE_8BIT = GerenciadorFontes.carregar("/Fonts/Jersey10-Regular.ttf");
        ControladorFluxo.iniciar(stage);

        // JogoEngine bmtl = new JogoEngine("bmtl", stage); <- SER CHAMADO QUANDO CLICAR NA MUSICA
        // bmtl.iniciar(25);
    }

    public static void main(String[] args) {
        launch();
    }
}
