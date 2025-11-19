package com.example.projetopoo;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ControladorFluxo.iniciar(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
