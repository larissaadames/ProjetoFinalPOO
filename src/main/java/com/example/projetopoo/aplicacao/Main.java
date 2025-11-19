package com.example.projetopoo.aplicacao;

import com.example.projetopoo.ControladorFluxo;
import com.example.projetopoo.GerenciadorFontes;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String NOME_FONTE_8BIT = GerenciadorFontes.carregar("/Fonts/Jersey10-Regular.ttf");
        ControladorFluxo.iniciar(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
