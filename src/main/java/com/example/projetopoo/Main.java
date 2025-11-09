package com.example.projetopoo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Group root = new Group();
        Scene cena = new Scene(root, 960, 540);
        stage.setTitle("Gameplay");
        stage.setFullScreen(true);
        stage.setScene(cena);
        stage.show();

        Jogo jogo = new Jogo(root);
        jogo.iniciar();


        
    }
}
