package com.example.projetopoo.jogo;

public class NotaHold extends Nota {
    public NotaHold(int lane, double momentoHit) {
        super(lane, momentoHit);
    }

    @Override
    public void tentaHit(long momentoAtualMusicaMs) {

    }

    @Override
    public void atualizar(double deltaTime, double tempoMusicaMs) {

    }

    @Override
    public void update(double deltaTime, double momentoAtualMusica) {

    }

    @Override
    public boolean checarHit() {
        return false;
    }
}
