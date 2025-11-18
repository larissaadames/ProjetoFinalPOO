package com.example.projetopoo.jogo;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

import java.io.IOException;

public class JogoEngine {

    private final DeltaTime timer = new DeltaTime();
    private final JogoLogica logica;
    private final JogoRenderer renderer = new JogoRenderer();
    private final JogoMusica musica;
    private JogoChart chart;

    public JogoEngine(String nomeMusica, Stage stage) throws IOException {

        String caminhoMusica = "/musics/" + nomeMusica + ".mp3";
        this.musica = new JogoMusica(caminhoMusica);

        String caminhoChart =  "/charts/" + nomeMusica + ".json";
        chart = CarregaJogoChart.carregar(caminhoChart);

        this.logica = new JogoLogica(this.chart);
        renderer.iniciarCena(stage);
    }


    public void iniciar() {

        InputHandler inputHandler = new InputHandler(logica, renderer, musica);
        inputHandler.ativar(renderer.getRoot().getScene());
        musica.play();

        AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long agora) {
                timer.atualizar(agora);
                double deltaTime = timer.getDeltatime();
                logica.atualizar(deltaTime, musica.getTempoMusicaMs());
                renderer.atualizar(logica, musica.getTempoMusicaMs(), deltaTime);
//               System.out.println("Ativas: " + logica.getNotasAtivas().size());
            }
        };

        gameLoop.start();
    }
}
