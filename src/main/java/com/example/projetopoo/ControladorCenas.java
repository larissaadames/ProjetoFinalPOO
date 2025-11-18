package com.example.projetopoo;

import com.example.projetopoo.jogo.core.JogoEstado;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ControladorCenas {

    protected Stage stage;
    private Scene cena;
    private Group root;
    private String fxml;
    ControladorCenas(Stage stage, Scene cena, Group root) {
        this.stage = stage;
        this.cena = cena;
        this.root = root;
    }
    ControladorCenas(Stage stage){
        this.stage = stage;
    }
    ControladorCenas(){}

    public static void carregarCena(String fxml, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ControladorCenas.class.getResource(fxml));
        Parent root = loader.load();
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }

    public static void irParaResultados(Stage stage, JogoEstado estadoFinal) throws IOException {
        FXMLLoader loader = new FXMLLoader(ControladorCenas.class.getResource("Resultados.fxml"));
        Parent root = loader.load();

        ResultadosController controller = loader.getController();

        controller.setDadosFinais(estadoFinal);

        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }

    public Parent getRoot(){
        return root;
    }

}
