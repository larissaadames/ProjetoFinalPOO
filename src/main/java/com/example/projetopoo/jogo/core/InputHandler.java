package com.example.projetopoo.jogo.core;


import com.example.projetopoo.ArduinoConexao;
import com.example.projetopoo.jogo.logica.JogoLogica;
import com.example.projetopoo.jogo.logica.Julgamento;
import com.example.projetopoo.jogo.notas.Nota;
import com.example.projetopoo.jogo.notas.NotaEstado;
import com.example.projetopoo.jogo.render.HitDot;
import com.example.projetopoo.jogo.render.JogoRenderer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.List;

public class InputHandler {

    private final JogoLogica logica;
    private final JogoRenderer renderer;
    private final JogoMusica musica;
    private final JogoEstado estado;
    private double offsetMs = 0;

    private Scene sceneAtual;

    // Index 0 não usado, índices 1-5 representam as lanes.
    private final boolean[] teclasPressionadas = new boolean[6];

    public InputHandler(JogoLogica logica, JogoRenderer renderer, JogoMusica musica, JogoEstado estado) {//construtor kabum
        this.logica = logica;
        this.renderer = renderer;
        this.musica = musica;
        this.estado = estado;
    }


    public void receberInputArduino(int lane, boolean pressionado) {
        if (lane < 1 || lane > 5) return;

        if (pressionado) {
            // Lógica de APERTAR
            // Só ativa se já não estiver pressionada (evita repetição)
            if (!teclasPressionadas[lane]) {
                teclasPressionadas[lane] = true;
                checarHit(lane);
            }
        } else {
            // Lógica de SOLTAR (Faltava isso!)
            // Quando receber o sinal de soltar ('d', 'f', etc), desliga a tecla.
            teclasPressionadas[lane] = false;
        }
    }
    public void setOffset(double offsetMs){
        this.offsetMs = offsetMs;
    }

    public void ativar(Scene scene) {
        scene.setOnKeyPressed(event -> {

            int lane = teclaParaLane(event.getCode());
            if (lane == -1) return;

            // Se a tecla já não estava pressionada (evita spam do evento)
            if (!teclasPressionadas[lane]) {
                teclasPressionadas[lane] = true;
                checarHit(lane);
            }
        });

        scene.setOnKeyReleased(event -> {
            int lane = teclaParaLane(event.getCode());
            if (lane == -1) return;

            teclasPressionadas[lane] = false;
        });
    }

    private int teclaParaLane(KeyCode code) {
        return switch (code) {
            case D -> 1;
            case F -> 2;
            case J -> 3;
            case K -> 4;
            case L -> 5;
            default -> -1;
        };
    }

    private void checarHit(int lane) {
        boolean acerto = false;

        double tempoCorrigido = musica.getTempoMusicaMs() - offsetMs;

        for (Nota nota : logica.getNotasAtivas()) {
            if (nota.getLane() == lane && nota.isAtiva()) {

                nota.tentaHit(tempoCorrigido);

                if (nota.getJulgamento() != null && nota.getEstado() != NotaEstado.ERROU) {
                    acerto = true;
                    break;
                }
            }
        }

        List<HitDot> dots = renderer.getHitDots();
        for (int i = 0; i < dots.size(); i++) {
            HitDot dot = dots.get(i);
            if (dot.getLane() == lane) {
                dot.piscar(acerto);
                break;
            }
        }
    }

    public void atualizarHolds() {
        double tempoAtual = musica.getTempoMusicaMs() - offsetMs;

        List<HitDot> dots = renderer.getHitDots();
        for (int i = 0; i < dots.size(); i++) {
            HitDot dot = dots.get(i);
            dot.manter(teclasPressionadas[dot.getLane()]);
        }

        for (Nota nota : logica.getNotasAtivas()) {
            if (nota.isAtiva() && teclasPressionadas[nota.getLane()]) {
                nota.segurar(tempoAtual);
            }

            if (nota.checarTick(tempoAtual)) {
                estado.adicionarPontosTick();
            }
        }

    }

    public void desativar() {
        if (sceneAtual != null) {
            // Remove os "ouvintes" da cena para parar de detectar teclado
            sceneAtual.setOnKeyPressed(null);
            sceneAtual.setOnKeyReleased(null);
            sceneAtual = null; // Limpa a referência
        }

        // Reseta o estado das teclas para evitar bugs se reentrar no jogo
        for(int i=0; i < teclasPressionadas.length; i++) {
            teclasPressionadas[i] = false;
        }
    }
}