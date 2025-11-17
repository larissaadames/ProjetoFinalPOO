package com.example.projetopoo.jogo;

public abstract class Nota {
    private double x;
    private double y;
    private boolean ativa = true;

    protected static final double SCROLL_SPEED = 0.25;
    protected static final double SPAWN_OFFSET_MS = 2000;
    protected final double momentoHit;
    protected final int lane;

    protected NotaEstado estado = NotaEstado.PENDENTE;
    protected Julgamento julgamento = null;

    protected double windowHitPerfeito = 40;
    protected double windowHitOtimo = 80;
    protected double windowHitRuim = 120;

    public Nota(int lane, double momentoHit){
        this.momentoHit = momentoHit;
        this.lane = lane;
        this.x = getLaneX();
        this.y = -200;
    }

    public abstract void tentaHit(long momentoAtualMusicaMs);

    public void onHit(double momentoAtualMusicaMs) {
        if(estado != NotaEstado.PENDENTE) return;

        estado = NotaEstado.ACERTO;
        double diff = Math.abs(momentoAtualMusicaMs - momentoHit);

        if (diff <= windowHitPerfeito) {
            julgamento = Julgamento.PERFEITO;
        }
        else if (diff <= windowHitOtimo) {
            julgamento = Julgamento.OTIMO;
        }
        else if (diff <= windowHitRuim) {
            julgamento = Julgamento.RUIM;
        }
        else {
            estado = NotaEstado.ERROU;
            julgamento = Julgamento.ERRO;
        }
    }

    public void atualizaErro(long momentoAtualMusicaMs) {
        if(estado == NotaEstado.PENDENTE && momentoAtualMusicaMs > momentoHit + windowHitRuim){
            estado = NotaEstado.ERROU;
            julgamento = Julgamento.ERRO;
        }
    }

    public abstract void atualizar(double deltaTime, double tempoMusicaMs);

    public boolean deveDespawnar(double tempoMusicaMs) {

        if (estado == NotaEstado.ACERTO) {
            return true;
        }

        if (estado == NotaEstado.ERROU) {
            return true;
        }

        if (tempoMusicaMs > momentoHit + windowHitRuim) {
            return true;
        }

        if (y > 900) { // ajuste conforme seu layout
            return true;
        }

        return false;
    }




    public NotaEstado getEstado() { return estado; }

    public Julgamento getJulgamento() { return julgamento; }

    public double getMomentoHit() { return momentoHit; }

    public double getX() {return x;}

    public double getY() {return y;}

    public int getLane() {return lane;}

    public boolean isAtiva() {return ativa;}

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getLaneX() {
        return switch (lane) {
            case 0 -> 100;
            case 1 -> 200;
            case 2 -> 300;
            case 3 -> 400;
            case 4 -> 500;
            default -> 100;
        };
    }


}


