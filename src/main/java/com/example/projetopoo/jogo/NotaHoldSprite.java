package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class NotaHoldSprite implements INotaSprite {

    private NotaHold nota;
    private final Group group; // Grupo para juntar Cabeça + Cauda
    private final Circle head; // Cabeça redondinha
    private final Rectangle tail; // Cauda arredondada

    public NotaHoldSprite(NotaHold nota) {
        this.nota = nota;

        double tailWidth = Layout.RAIO_CIRCLE * 1.6;
        this.tail = new Rectangle(tailWidth, 0);

        this.tail.setArcWidth(50);
        this.tail.setArcHeight(50);
        this.tail.setOpacity(0.6);

        this.head = new Circle(Layout.RAIO_CIRCLE);
        this.head.setStroke(Color.WHITE);
        this.head.setStrokeWidth(4);

        this.group = new Group(tail, head);

        atualizarCor(Layout.getCorLane(nota.getLane()));
    }

    private void atualizarCor(Color corBase) {
        head.setFill(corBase);
        tail.setFill(corBase);
    }


    @Override
    public void atualizar(double tempoMusicaMs) {
        double yHead = nota.getY();
        double x = nota.getLaneX();

        head.setLayoutX(x);
        head.setLayoutY(yHead);

        double tempoFinal = nota.getMomentoHit() + nota.getDuracaoMs();
        double tempoRestanteVisivel = tempoFinal - tempoMusicaMs;

        double yTailTop = Layout.HIT_LINE - (tempoRestanteVisivel * Nota.SCROLL_SPEED);

        double alturaTail = yHead - yTailTop;

        double alturaMaxima = nota.getDuracaoMs() * Nota.SCROLL_SPEED;
        alturaTail = Math.max(0, Math.min(alturaTail, alturaMaxima));

        tail.setHeight(alturaTail);
        tail.setLayoutX(x - (tail.getWidth() / 2));

        tail.setLayoutY(yHead - alturaTail + 5);
    }

    @Override
    public void reusar(NotaHold novaNota) {
        this.nota = novaNota;
        Color cor = Layout.getCorLane(novaNota.getLane());
        head.setFill(cor);
        tail.setFill(cor);
        atualizar(0);
    }

    @Override
    public void reusar(NotaTap nota) {
        throw new UnsupportedOperationException("HoldSprite não pode reusar NotaTap.");
    }

    @Override
    public Node getNode() {
        return group;
    }

    @Override
    public Nota getNota() {
        return nota;
    }

    @Override
    public void devolverPara(JogoRenderer renderer) {
        renderer.reciclarHold(this);
    }

}