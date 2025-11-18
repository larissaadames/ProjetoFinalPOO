package com.example.projetopoo.jogo;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

import java.io.IOException;

public class JogoEngine {

    private final DeltaTime timer = new DeltaTime();
    private final JogoLogica logica;
    private final JogoRenderer renderer;
    private final JogoMusica musica;
    private final JogoEstado estado;

    public JogoEngine(String nomeMusica, Stage stage) throws IOException {

        String caminhoMusica = "/musics/" + nomeMusica + ".mp3";
        this.musica = new JogoMusica(caminhoMusica);

        String caminhoChart =  "/charts/" + nomeMusica + ".json";
        JogoChart chart = CarregaJogoChart.carregar(caminhoChart);

        this.logica = new JogoLogica(chart);
        this.estado = new JogoEstado();
        this.renderer = new JogoRenderer(this.estado);

        renderer.iniciarCena(stage);
    }


    public void iniciar() {

        InputHandler inputHandler = new InputHandler(logica, renderer, musica, estado);        inputHandler.ativar(renderer.getRoot().getScene());
        musica.play();

        AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long agora) {
                timer.atualizar(agora);
                double deltaTime = timer.getDeltatime();
                inputHandler.atualizarHolds();
                logica.atualizar(deltaTime, musica.getTempoMusicaMs());

                for (Nota nota : logica.getNotasFinalizadasNesteFrame()) {
                    estado.registrarHit(nota.getJulgamento());

                    renderer.mostrarFeedback(nota.getJulgamento());
                }

                renderer.atualizar(logica, musica.getTempoMusicaMs(), deltaTime, timer.getFps());
//               System.out.println("Ativas: " + logica.getNotasAtivas().size());
            }
        };

        gameLoop.start();
    }
}
