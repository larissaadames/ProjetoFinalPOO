package com.example.projetopoo.jogo;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NotaSpriteTap extends NotaSprite {

    private final NotaTap nota;
    private final Circle circle;


    public NotaSpriteTap(NotaTap nota) {
        this.nota = nota;

        this.circle = new Circle(Layout.RAIO_CIRCLE);
        circle.setFill(Color.CYAN);
        circle.setLayoutX(nota.getLaneX());
        circle.setLayoutY(nota.getY());
    }

    @Override
    public void atualizarSprite(double deltaTime, double tempoMusicaMs) {
        circle.setLayoutY(nota.getY());
    }

    @Override
    public Node getNode() {
        return circle;
    }
}
