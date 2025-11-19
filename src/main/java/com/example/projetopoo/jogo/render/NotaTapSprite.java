package com.example.projetopoo.jogo.render;

import com.example.projetopoo.jogo.core.Layout;
import com.example.projetopoo.jogo.notas.Nota;
import com.example.projetopoo.jogo.notas.NotaHold;
import com.example.projetopoo.jogo.notas.NotaTap;
import javafx.scene.Node;
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

    @Override
    public void reusar(NotaTap novaNota) {
        this.nota = novaNota;
        this.circle.setFill(Layout.getCorLane(novaNota.getLane()));
        atualizar(0);
    }

    @Override
    public void reusar(NotaHold nota) { // CREDO ! ! !
        // Esta chamada nunca deve ocorrer na lógica correta do pool.
        throw new UnsupportedOperationException("TapSprite não pode reusar NotaHold.");
    }

    @Override
    public void devolverPara(JogoRenderer renderer) {
        renderer.reciclarTap(this); // Sem cast, sem instanceof!
    }
}
