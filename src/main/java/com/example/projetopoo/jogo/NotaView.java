package com.example.projetopoo.jogo;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class NotaView {
    private Nota nota;
    private Circle circle;

    public Node getNode() {
        return circle;
    }

    public void atualizar(double musicTime) {

    }
}
