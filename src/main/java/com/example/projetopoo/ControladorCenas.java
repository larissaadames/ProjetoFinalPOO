package com.example.projetopoo;

import com.example.projetopoo.jogo.Jogo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    public void mudarCenaPorBotao(Button botao, String fxml) throws IOException {
        Stage stage = (Stage) botao.getScene().getWindow();
        carregarCena(fxml, stage);
    }

    public void iniciarJogo() {
        Jogo jogo = new Jogo();
        jogo.iniciar();
    }

    public Parent getRoot(){
        return root;
    }

}
