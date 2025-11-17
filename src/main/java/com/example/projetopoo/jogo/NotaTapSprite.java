package com.example.projetopoo.jogo;

import javafx.scene.shape.Circle;

public class NotaTapSprite {

    private Nota nota;
    private Circle circle;

    public NotaTapSprite(Nota nota, Circle circle) {
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
