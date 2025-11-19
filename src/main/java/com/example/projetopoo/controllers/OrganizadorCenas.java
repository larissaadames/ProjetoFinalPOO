package com.example.projetopoo.controllers;

import javafx.application.Platform;
import javafx.scene.Node;

public abstract class OrganizadorCenas {

    // Contrato para liberar recursos ao sair
    public abstract void exitSceneCleanup();

    /**
     * Configuração padrão pós-carregamento.
     * Garante que a raiz da cena receba o foco do teclado.
     */
    public void postLoadSetup(Node root) {
        if (root != null) {
            Platform.runLater(root::requestFocus);
        }
    }
}