package com.example.projetopoo.jogo;

public class NotaTap extends Nota {

    @Override
    public void tentaHit(long momentoAtualMusicaMs) {

    }

    @Override
    public void atualizar(double deltaTime) {
        this.setX(this.getX() + 10);
        this.setY(this.getY() + 10);
    }
}
