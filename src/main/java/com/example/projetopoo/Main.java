package com.example.projetopoo;

import com.example.projetopoo.jogo.JogoEngine;
import javafx.application.Application;

import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JogoEngine engine = new JogoEngine("bmtl", stage);

        engine.iniciar();


    }
}
