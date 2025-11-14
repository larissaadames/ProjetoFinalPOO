package com.example.projetopoo.jogo;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

public abstract class Nota {
    private double hitTime; // ms
    private double scrollSpeed;
    private double tempoStart;
    private boolean ativa = true;

    // esses enums sao formas boas de organizar e salvar
    public enum NotaEstado {
        PENDENTE,
        ACERTO,
        SEGURANDO,
        SOLTA,
        ERROU
    }

    public enum Julgamento {
        PERFEITO,
        OTIMO,
        RUIM,
        ERRO
    }

    protected final long momentoHit;
    protected final int lane;
    protected NotaEstado estado = NotaEstado.PENDENTE;
    protected Julgamento julgamento = null;
    protected long windowHitPerfeito = 40;
    protected long windowHitOtimo = 80;
    protected long windowHitRuim = 120;

    public Nota(long momentoHit, int lane){
        this.momentoHit = momentoHit;
        this.lane = lane;
    }

    public abstract void tentaHit(long momentoAtualMusicaMs);
    public void atualizaErro(long momentoAtualMusicaMs) {
        if(estado == NotaEstado.PENDENTE && momentoAtualMusicaMs > momentoHit + windowHitRuim){
            estado = NotaEstado.ERROU;
            julgamento = Julgamento.ERRO;
        }
    }


    public abstract void update(double deltaTime, double momentoAtualMusica);

//    public void onHit() {
//        hit = true;
//        ativa = false;
//        view.setVisible(false);
//    }
//
//    public void onMiss() {
//        ativa = false;
//        view.setVisible(false);
//    }

    public NotaEstado getEstado() { return estado; }
    public Julgamento getJulgamento() { return julgamento; }
    public long getMomentoHit() { return momentoHit; }
    public double getHitTime() { return hitTime; }
    public double getScrollSpeed() {return scrollSpeed;}
    public double getTempoStart() {return tempoStart;}
    public int getLane() {return lane;}
    public boolean isAtiva() {return ativa;}

//    Nota(double posX,double posY,String cor, float scrollSpeed) {
//        circle = new Circle(posX, posY, 25, Color.web(cor));
//        this.scrollSpeed = scrollSpeed;
//
//    }

//
//    public Circle getCircle() {
//        return circle;
//    }
//
//    public void moverNota(double deltaTime) {
//        this.circle.setCenterY(this.circle.getCenterY() + scrollSpeed * deltaTime);
//    }
//
//    public void foraDatela() {
//        if(this.circle.getCenterY() >= 1000) this.circle.setCenterY(100);
//    }

}


