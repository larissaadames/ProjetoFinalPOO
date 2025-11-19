package com.example.projetopoo;

import javafx.fxml.FXML;

public abstract class OrganizadorCenas {

    // Contrato para liberar recursos (mídia, timers) ao sair da cena.
    public abstract void exitSceneCleanup();

    // Pode ser usado para configuração após a injeção FXML.
    @FXML
    public void postLoadSetup() {
        // Metodo de convenção para setup.
    }
}