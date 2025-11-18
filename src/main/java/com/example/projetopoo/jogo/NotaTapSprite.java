package com.example.projetopoo.jogo;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NotaTapSprite implements INotaSprite {

    private final Nota nota;
    private final Circle circle;

    public NotaTapSprite(Nota nota) {
        this.nota = nota;
        this.circle = new Circle(Layout.RAIO_CIRCLE);
        this.circle.setFill(Color.CYAN);
        atualizar();
    }

    @Override
    public void atualizar() {
        circle.setLayoutX(nota.getLaneX());
        circle.setLayoutY(nota.getY());
    }

    @Override
    public Node getNode() { return circle; }

    @Override
    public Nota getNota() { return nota; }
}
