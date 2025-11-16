package com.example.projetopoo.jogo;

import javafx.animation.AnimationTimer;

import java.io.IOException;

public class JogoEngine {

    private final DeltaTime timer = new DeltaTime();
    private final JogoLogica logica;
    private final JogoRenderer render = new JogoRenderer();
    private final JogoMusica musica;
    private JogoChart chart;

    public JogoEngine(String nomeMusica) throws IOException {

        String caminhoMusica = "src/main/resources/musics/" + nomeMusica + ".mp3";
        this.musica = new JogoMusica(caminhoMusica);

        String caminhoChart =  "src/main/resources/charts/" + nomeMusica + ".json";
        chart = CarregaJogoChart.carregar(caminhoChart);

        this.logica = new JogoLogica(this.chart);
        this.chart = CarregaJogoChart.carregar(caminhoChart);
    }

    public void iniciar() {

        musica.play();

        AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long agora) {
                timer.atualizar(agora);
                double deltaTime = timer.getDeltatime();
                logica.atualizar(deltaTime, musica.getTempoMusicaMs());
                // renderer.update
            }
        };

        gameLoop.start();
    }
}
