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
    public void atualizar(double tempoMusicaMs) { // Recebe o tempo
        double yHead = nota.getY(); // A cabeça já está no lugar certo (tratado pela lógica da Nota)
        double x = nota.getLaneX();

        // 1. Posiciona a Cabeça
        head.setLayoutX(x);
        head.setLayoutY(yHead);

        // 2. Calcula onde deveria estar o FIM (Topo) da nota no mundo
        // Fórmula: HitLine - (TempoFinal - TempoAtual) * Velocidade
        double tempoFinal = nota.getMomentoHit() + nota.getDuracaoMs();
        double tempoRestanteVisivel = tempoFinal - tempoMusicaMs;

        // O topo da cauda é calculado em relação à HitLine
        double yTailTop = Layout.HIT_LINE - (tempoRestanteVisivel * Nota.SCROLL_SPEED);

        // A altura é a distância entre a Cabeça (embaixo) e o Topo (em cima)
        // Nota: No JavaFX o Y cresce para baixo, então yHead é maior que yTailTop
        double alturaTail = yHead - yTailTop;

        // Garante que não fique negativo nem gigante (bug visual)
        // Limitamos ao tamanho máximo original para evitar glitches quando a nota nasce
        double alturaMaxima = nota.getDuracaoMs() * Nota.SCROLL_SPEED;
        alturaTail = Math.max(0, Math.min(alturaTail, alturaMaxima));

        // 3. Atualiza a Cauda
        tail.setHeight(alturaTail);
        tail.setLayoutX(x - (tail.getWidth() / 2));

        // A cauda desenha do topo para baixo, então posicionamos no topo calculado
        // Mas para garantir que ela "grude" na cabeça, usamos (yHead - altura)
        tail.setLayoutY(yHead - alturaTail + 5); // +5 para overlap visual
    }

    @Override
    public Node getNode() {
        return group;
    }

    @Override
    public Nota getNota() {
        return nota;
    }

}