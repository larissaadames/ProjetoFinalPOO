package com.example.projetopoo.jogo.core;


//imports fodas🔥🔥
import com.example.projetopoo.ArduinoConexao;
import com.example.projetopoo.jogo.chart.CarregaJogoChart;
import com.example.projetopoo.jogo.chart.JogoChart;
import com.example.projetopoo.jogo.logica.JogoLogica;
import com.example.projetopoo.jogo.notas.Nota;
import com.example.projetopoo.jogo.render.JogoRenderer;
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

    private ArduinoConexao arduino;

    public JogoEngine(String nomeMusica, Stage stage) throws IOException {
        this.stage = stage;

        String caminhoMusica = "/musics/" + nomeMusica + ".mp3";
        this.musica = new JogoMusica(caminhoMusica);

        String caminhoChart =  "/charts/" + nomeMusica + ".json";
        JogoChart chart = CarregaJogoChart.carregar(caminhoChart);

        this.logica = new JogoLogica(chart);
        this.estado = new JogoEstado();
        this.renderer = new JogoRenderer(this.estado);


        renderer.iniciarCena(stage);
        //this.musica.setAcaoFimMusica(this::finalizarRun);
    }

//    public void finalizarRun() {
//        if (gameLoop != null) gameLoop.stop();
//        musica.stop();
//
//        try {
//            ControladorFluxo.irParaResultados(stage, this.estado);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void iniciar(double offsetSegundos) {

        double offsetMs = offsetSegundos * 1000;

        logica.pularParaTempo(offsetMs);

        InputHandler inputHandler = new InputHandler(logica, renderer, musica, estado);
        inputHandler.ativar(renderer.getRoot().getScene());


        this.arduino = new ArduinoConexao(inputHandler);
        this.arduino.iniciar();


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
