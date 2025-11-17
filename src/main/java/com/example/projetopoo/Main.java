package com.example.projetopoo;

import com.example.projetopoo.jogo.JogoEngine;
import javafx.application.Application;
import javafx.fxml.FXML;
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
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JogoEngine engine = new JogoEngine("bmtl", stage);

        engine.iniciar();


//        ControladorCenas controlador = new ControladorCenas(stage);
//        MenuController menuController = new MenuController();
//        menuController.setStage(stage);
//        ControladorCenas.carregarCena("Menu.fxml", stage);



//        Parent rootMenu = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Menu.fxml")));
//        Scene cenaMenu = new Scene(rootMenu);
//        stage.setScene(cenaMenu);
//        stage.show();
//        stage.setFullScreen(true);
//        Button botaoJogar = new Button("JOGAR");
//        botaoJogar.setPrefWidth(300);
//        botaoJogar.setPrefHeight(250);
//        botaoJogar.setLayoutX(960);
//        botaoJogar.setLayoutY(540);


//        Text titulo = new Text("POWERJORGE");

//        rootMenu.getChildren().add(botaoJogar);
//        rootMenu.getChildren().add(titulo);


//        Group root = new Group();
//        Scene cenaJogo = new Scene(root, 1920, 1080);
//        stage.setTitle("Gameplay");
//        stage.setFullScreen(true);

//        botaoJogar.setOnAction(evento -> stage.setScene(cenaJogo) );
//        // stage.setScene(cenaJogo);
//
//
//
//
//        Jogo jogo = new Jogo(root);
//        jogo.iniciar();



    }
}
