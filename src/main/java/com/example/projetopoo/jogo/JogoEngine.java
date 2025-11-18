package com.example.projetopoo.jogo;

import com.example.projetopoo.ControladorCenas;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

import java.io.IOException;

public class JogoEngine {

    private final DeltaTime timer = new DeltaTime();
    private final JogoLogica logica;
    private final JogoRenderer renderer;
    private final JogoMusica musica;
    private final JogoEstado estado;
    private final Stage stage;
    private AnimationTimer gameLoop;

    public JogoEngine(String nomeMusica, Stage stage) throws IOException {
        this.stage = stage;

        String caminhoMusica = "/musics/" + nomeMusica + ".mp3";
        this.musica = new JogoMusica(caminhoMusica);

        String caminhoChart =  "/charts/" + nomeMusica + ".json";
        JogoChart chart = CarregaJogoChart.carregar(caminhoChart);

        this.logica = new JogoLogica(chart);
        this.estado = new JogoEstado();
        this.renderer = new JogoRenderer(this.estado);
        this.musica.setAcaoFimMusica(this::finalizarJogo);

        renderer.iniciarCena(stage);
    }

    public void finalizarJogo() {
        if (gameLoop != null) gameLoop.stop();
        musica.stop();

        try {
            ControladorCenas.irParaResultados(stage, this.estado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void iniciar(double offsetSegundos) {

        double offsetMs = offsetSegundos * 1000;

        logica.pularParaTempo(offsetMs);

        InputHandler inputHandler = new InputHandler(logica, renderer, musica, estado);
        inputHandler.ativar(renderer.getRoot().getScene());

        musica.iniciarComOffset(offsetMs);
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
