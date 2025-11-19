package com.example.projetopoo.jogo.core;

//imports fogos🔥🔥
import com.example.projetopoo.ArduinoConexao;
import com.example.projetopoo.ControladorFluxo;
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
    private final Stage stage; // <-- não entendo pq essa porra ta cinza
    private double globalOffsetMs = 100;

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

        this.musica.setAcaoFimMusica(this::finalizarJogo);

        renderer.iniciarCena(stage);
        //this.musica.setAcaoFimMusica(this::finalizarRun);
    }

//    public void finalizarRun() { // <--- TEM KI ARRUMAR LOGO
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

        double skipIntroMs = offsetSegundos * 1000;

        // 1. Prepara a lógica
        logica.pularParaTempo(skipIntroMs);

        // 2. Configura Inputs e Latência
        InputHandler inputHandler = new InputHandler(logica, renderer, musica, estado);
        inputHandler.setOffset(globalOffsetMs);
        inputHandler.ativar(renderer.getRoot().getScene());

        // 3. Inicia Arduino
        this.arduino = new ArduinoConexao(inputHandler); // <-- NÃO MEXER NESSA BRINCADEIRINHA
        this.arduino.iniciar();

        // 4. Inicia Música
        musica.iniciarComOffset(skipIntroMs);
        musica.play();

        // 5. Loop do Jogo (Limpo)
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long agora) {
                // A. Calcula tempo e delta
                timer.atualizar(agora);
                double deltaTime = timer.getDeltatime();

                // B. Calcula tempo corrigido (Latência)
                double tempoJogo = musica.getTempoMusicaMs() - globalOffsetMs;

                // C. Atualiza Inputs (Arduino/Teclado)
                inputHandler.atualizarHolds();

                // D. Atualiza Lógica e Renderização (uma vez por frame, com tempo corrigido)
                logica.atualizar(deltaTime, tempoJogo);

                for (Nota nota : logica.getNotasFinalizadasNesteFrame()) {
                    estado.registrarHit(nota.getJulgamento());
                    renderer.mostrarFeedback(nota.getJulgamento());
                }

                renderer.atualizar(logica, tempoJogo, deltaTime, timer.getFps());
            }
        };
        gameLoop.start();
    }

    private void finalizarJogo() {

//        gameLoop.stop();

        musica.stop();

            ControladorFluxo.irParaTelaFinal(this.nomeMusica, this.estado);
    }
}
