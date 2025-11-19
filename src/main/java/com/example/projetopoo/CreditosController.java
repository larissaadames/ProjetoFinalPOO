package com.example.projetopoo;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class CreditosController extends OrganizadorCenas {

    @FXML private AnchorPane root;

    @FXML
    public void initialize() {
        // Garante o foco na tela para receber comandos do teclado
        postLoadSetup(root);

        // Configura o ESC para voltar
        root.setOnKeyPressed(this::handleKey);
    }

    private void handleKey(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE, ENTER, SPACE -> ControladorFluxo.irParaMenu();
        }
    }

    @Override
    public void exitSceneCleanup() {
        // Nada para limpar nesta cena
    }
}