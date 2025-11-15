package com.example.projetopoo.jogo;

import javafx.animation.AnimationTimer;

public class JogoEngine {

    private final DeltaTime timer = new DeltaTime();
    private final JogoLogica logic = new JogoLogica();

    AnimationTimer loop = new AnimationTimer() {

        @Override
        public void handle(long agora) {
            timer.atualizar(agora);
            double deltatime = timer.getDeltatime();
            logic.atualizar();
            // renderer.update
        }
    };
}
