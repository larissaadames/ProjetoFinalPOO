package com.example.projetopoo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Parent rootSeletorMusica = FXMLLoader.load
                (getClass().getResource("cenaSeletorMusica.fxml"));


        Scene cenaSeletor = new Scene(rootSeletorMusica);
        stage.setScene(cenaSeletor);
        stage.show();
//        Group rootMenu = new Group();
//        Scene cenaMenu = new Scene(rootMenu, 1920,1080);
//        Button botaoJogar = new Button("JOGAR");
//        botaoJogar.setPrefWidth(300);
//        botaoJogar.setPrefHeight(250);
//        botaoJogar.setLayoutX(960);
//        botaoJogar.setLayoutY(540);
//
//
//        Text titulo = new Text("POWERJORGE");
//
//        rootMenu.getChildren().add(botaoJogar);
//        rootMenu.getChildren().add(titulo);
//        stage.setScene(cenaMenu);
//
//        Group root = new Group();
//        Scene cenaJogo = new Scene(root, 1920, 1080);
//        stage.setTitle("Gameplay");
//        stage.setFullScreen(true);
//
//        botaoJogar.setOnAction(evento -> stage.setScene(cenaJogo) );
//        // stage.setScene(cenaJogo);
//        stage.show();
//
//
//
//        Jogo jogo = new Jogo(root);
//        jogo.iniciar();


        
    }
}
