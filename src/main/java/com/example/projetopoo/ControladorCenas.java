package com.example.projetopoo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class ControladorCenas {

    private Stage stage;
    private Scene cena;
    private Parent root;
    private String fxml;

    public static void carregarCena(String fxml, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ControladorCenas.class.getResource(fxml));
        Parent root = loader.load();
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }

    public static void mudarCenaPorBotao(Button botao, String fxml) throws IOException {
        Stage stage = (Stage) botao.getScene().getWindow();
        carregarCena(fxml, stage);
    }

    public Parent getRoot(){
        return root;
    }


}
