package com.example.projetopoo.jogo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NotaSprite {

    private Nota nota;
    private Circle circle;

    public NotaSprite(Nota nota, Circle circle) {
        this.nota = nota;
        this.circle = circle;
    }

    public Nota getNota() {
        return nota;
    }

    public Circle getCircle() {
        return circle;
    }
}
