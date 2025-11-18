package com.example.projetopoo;

import com.example.projetopoo.jogo.core.JogoEngine;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JogoEngine bmtl = new JogoEngine("bmtl", stage);

        bmtl.iniciar(220);

    }
}
