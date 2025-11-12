package com.example.projetopoo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button botaoJogar;

    @FXML
    private Button botaoSair;

    Jogo jogo = new Jogo(ControladorCenas.);
    jogo.iniciar();
}
