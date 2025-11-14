package com.example.projetopoo.jogo;

public class NotaHold extends Nota {
    public NotaHold(double hitTime, int lane) {
        super(hitTime, lane);
    }

    @Override
    public void update(double deltaTime, double momentoAtualMusica) {

    }

    @Override
    public boolean checarHit() {
        return false;
    }
}
