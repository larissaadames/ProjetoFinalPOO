package com.example.projetopoo.jogo;

import javafx.animation.AnimationTimer;

public class DeltaTime {
    private double deltatime;
    private double fps;
    private double ultimoTempo;

    public void atualizar(long agora){

            if (ultimoTempo == 0) {
                ultimoTempo = agora;
                return;
            }

            double deltatime = (agora - ultimoTempo) / 1_000_000_000.0;
            ultimoTempo = agora;

            fps = 1.0 / deltatime;
        }

    public double getDeltatime(){
        return deltatime;
    }
    public double getFps(){
        return fps;
    }

}
