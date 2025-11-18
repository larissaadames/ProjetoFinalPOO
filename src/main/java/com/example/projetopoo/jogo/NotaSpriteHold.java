package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NotaSpriteHold extends NotaSprite {

    private final NotaHold nota;
    private final Rectangle head;
    private final Rectangle tail;
    private final Group group;

    public NotaSpriteHold(NotaHold nota) {
        this.nota = nota;

        this.head = new Rectangle(80, 20);
        head.setFill(Color.LIGHTGREEN);

        this.tail = new Rectangle(80, nota.getDuracaoMs() * Nota.SCROLL_SPEED);
        tail.setFill(Color.GREEN);

        this.group = new Group(tail, head);
        group.setLayoutX(nota.getLaneX());
        group.setLayoutY(nota.getY());
    }

    @Override
    public void atualizarSprite(double deltaTime, double tempoMusicaMs) {
        group.setLayoutX(nota.getLaneX());
        group.setLayoutY(nota.getY());
        tail.setHeight(nota.getDuracaoMs() * Nota.SCROLL_SPEED);
    }

    @Override
    public Node getNode() {
        return group;
    }
}