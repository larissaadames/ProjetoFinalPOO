package com.example.projetopoo.jogo;

public abstract class Nota {
    private double x;
    private double y;
    private boolean ativa = true;
    private NotaTipo tipo;
    public abstract INotaSprite obterSprite(JogoRenderer renderer);

    protected static final double SCROLL_SPEED = 1.5;
    protected static final double SPAWN_OFFSET_MS = 2000;
    protected final double momentoHit;
    protected final int lane;

    protected NotaEstado estado = NotaEstado.PENDENTE;
    protected Julgamento julgamento;

    protected double windowHitPerfeito = 30;
    protected double windowHitOtimo = 60;
    protected double windowHitRuim = 90;

    public Nota(int lane, double momentoHit){
        this.momentoHit = momentoHit;
        this.lane = lane;
        this.x = getLaneX();
        this.y = -100;
    }

    public abstract void tentaHit(double momentoAtualMusicaMs);

    public void onHit(double momentoAtualMusicaMs) {
        if(estado != NotaEstado.PENDENTE) return;
        ativa = false;

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

    public void atualizaErro(double momentoAtualMusicaMs) {
        if(estado == NotaEstado.PENDENTE && momentoAtualMusicaMs > momentoHit + windowHitRuim){
            estado = NotaEstado.ERROU;
            julgamento = Julgamento.ERRO;
        }
    }

    public abstract void atualizar(double deltaTime, double tempoMusicaMs);

    public abstract void segurar(double tempoMusicaMs);


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

        if (y > Layout.HIT_LINE + 500) {
            return true;
        }

        if(!ativa) {
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

    public void setAtiva(boolean ativa) {this.ativa = ativa;}

    public double getLaneX() {

        return switch (lane) {
            case 1 -> Layout.INICIO_X;
            case 2 -> Layout.INICIO_X + 150;
            case 3 -> Layout.INICIO_X + 300;
            case 4 -> Layout.INICIO_X + 450;
            case 5 -> Layout.INICIO_X + 600;
            default -> Layout.INICIO_X;
        };
    }

    public NotaTipo getTipo() {
        return this.tipo;
    }

    public void setTipo(NotaTipo tipo) {
        this.tipo = tipo;
    }

    public boolean checarTick(double tempoMusicaMs) {
        return false;
    }

}


