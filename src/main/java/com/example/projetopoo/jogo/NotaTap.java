package com.example.projetopoo.jogo;

public class NotaTap extends Nota {

    public NotaTap(int lane, double momentoHit) {
        super(lane, momentoHit);
    }

    @Override
    public void tentaHit(long momentoAtualMusicaMs) {
        if (!isAtiva()) return;

        onHit(momentoAtualMusicaMs);
    }

    @Override
    public void atualizar(double deltaTime, double tempoMusicaMs) {

        double restante = momentoHit - tempoMusicaMs;

        double posY = (restante - SPAWN_OFFSET_MS) * SCROLL_SPEED;

        this.setY(posY);
    }
}
