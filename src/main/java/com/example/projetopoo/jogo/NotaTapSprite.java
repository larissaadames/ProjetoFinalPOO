package com.example.projetopoo.jogo;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NotaTapSprite implements INotaSprite {

    private Nota nota;
    private final Circle circle;

    public NotaTapSprite(Nota nota) {
        this.nota = nota;
        this.circle = new Circle(Layout.RAIO_CIRCLE);
        this.circle.setFill(Layout.getCorLane(nota.getLane()));
    }

    @Override
    public void atualizar(double tempoMusicaMs) {
        // aqui nao precisa de tempo por que ele nao usa posição, mas o pai implementou por causa da hold note
        circle.setLayoutX(nota.getLaneX());
        circle.setLayoutY(nota.getY());
    }

    @Override
    public Node getNode() { return circle; }

    @Override
    public Nota getNota() { return nota; }
}
