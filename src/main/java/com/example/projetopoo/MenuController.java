package com.example.projetopoo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Formatter;

public class MenuController extends ControladorCenas{

    @FXML
    private Button botaoJogar;
    @FXML
    private Button botaoSair;

    public void initialize() {
//        Stage stage = (Stage) botaoJogar.getScene().getWindow();

        botaoJogar.setOnAction(actionEvent -> {
            Stage stage = (Stage) botaoSair.getScene().getWindow(); // pega o stage que o botao esta e define como stage
            try {
                ControladorCenas.carregarCena("Teste.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        botaoSair.setOnAction(actionEvent -> {
            Stage stage = (Stage) botaoSair.getScene().getWindow();
            stage.close();
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
}
