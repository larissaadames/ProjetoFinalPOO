package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NotaHoldSprite implements INotaSprite {

    private final NotaHold nota;
    private final Group group;
    private final Rectangle head;
    private final Rectangle tail;

    public NotaHoldSprite(NotaHold nota) {
        this.nota = nota;

        this.head = new Rectangle(Layout.RAIO_CIRCLE * 2, Layout.RAIO_CIRCLE * 2);
        this.head.setFill(Color.LIGHTGREEN);
        this.head.setArcWidth(20);
        this.head.setArcHeight(20);

        this.tail = new Rectangle(50, 0);
        this.tail.setFill(Color.GREEN);
        this.tail.setOpacity(0.7);

        this.group = new Group(tail, head);
    }

    @Override
    public void atualizar() {
        double y = nota.getY();
        double x = nota.getLaneX() - Layout.RAIO_CIRCLE; // Centraliza retângulo

        head.setLayoutX(x);
        head.setLayoutY(y);

        double alturaTail = (nota.getDuracaoMs()) * Nota.SCROLL_SPEED; // Simplificado

        // cauda visual legal
        // double alturaTail = (nota.getMomentoHit() + nota.getDuracaoMs() - musicTime) ...

        tail.setLayoutX(x + (Layout.RAIO_CIRCLE - 25)); // Centraliza a cauda na lane
        tail.setLayoutY(y - alturaTail); // Desenha pra cima ou pra baixo dependendo da sua lógica
        tail.setHeight(alturaTail);
    }

    @Override
    public Node getNode() { return group; }

    @Override
    public Nota getNota() { return nota; }


}