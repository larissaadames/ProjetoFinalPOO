package com.example.projetopoo.jogo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NotaHoldSprite {
    private final NotaHold nota;
    private final Rectangle head; // círculo ou quadrado da “cabeça”
    private final Rectangle tail; // retângulo representando o hold

    public NotaHoldSprite(NotaHold nota, Rectangle head, Rectangle tail) {
        this.nota = nota;
        this.head = head;
        this.tail = tail;
    }

    public void atualizar() {
        double y = nota.getY();
        double x = nota.getLaneX();

        // atualiza posição da cabeça
        head.setLayoutX(x);
        head.setLayoutY(y);

        // atualiza tamanho do tail
        double alturaTail = Math.max(20, (nota.getMomentoHit() + nota.getDuracaoMs() - nota.getMomentoHit()) * Nota.SCROLL_SPEED);
        tail.setLayoutX(x);
        tail.setLayoutY(y); // alinhado com a cabeça
        tail.setHeight(alturaTail);
    }

    public Rectangle getHead() { return head; }
    public Rectangle getTail() { return tail; }
}