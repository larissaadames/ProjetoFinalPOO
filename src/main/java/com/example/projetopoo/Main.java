package com.example.projetopoo;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ControladorCenas.iniciar(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}
