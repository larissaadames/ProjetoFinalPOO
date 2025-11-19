package com.example.projetopoo.jogo.core;

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

    // Index 0 não usado, índices 1-5 representam as lanes.
    private final boolean[] teclasPressionadas = new boolean[6];

    public InputHandler(JogoLogica logica, JogoRenderer renderer, JogoMusica musica, JogoEstado estado) {
        this.logica = logica;
        this.renderer = renderer;
        this.musica = musica;
        this.estado = estado;
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

        for (Nota nota : logica.getNotasAtivas()) {
            if (nota.getLane() == lane && nota.isAtiva()) {
                nota.tentaHit(musica.getTempoMusicaMs());

                if (nota.getJulgamento() != null && nota.getEstado() != NotaEstado.ERROU) {
                    Julgamento j = nota.getJulgamento();

                    estado.registrarHit(j);

                    renderer.mostrarFeedback(j);

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
        double tempoAtual = musica.getTempoMusicaMs();

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
}