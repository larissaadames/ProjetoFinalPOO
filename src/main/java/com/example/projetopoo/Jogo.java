package com.example.projetopoo;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Parent;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Jogo {
    Group root = new Group();

    public void iniciar() {

        ArrayList<Nota> notas = new ArrayList<>();
        float testeX = 100;

        for(int i = 0; i < 10; i++) {
            notas.add(new Nota(testeX, 100, "FF00FF", 1000));
            testeX += 50;
        }

        for(Nota nota : notas) {
            root.getChildren().add(nota.getCircle());
        }

        AnimationTimer gameLoop = new AnimationTimer() {
            private double agora;
            private double ultimoTempo;

            @Override
            public void handle(long agora) {

                if (ultimoTempo == 0) {
                    ultimoTempo = agora;
                    return;
                }

                double deltaTime = (agora - ultimoTempo) / 1_000_000_000.0;
                ultimoTempo = agora;

                for(Nota nota : notas) {
                    nota.moverNota(deltaTime);
                    nota.foraDatela();
                }
            }
        };

        gameLoop.start();
    }
}
