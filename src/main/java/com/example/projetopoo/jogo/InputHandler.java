package com.example.projetopoo.jogo;

import javafx.scene.Scene;
import java.util.HashSet;
import java.util.Set;

public class InputHandler {

    private final JogoLogica logica;
    private final JogoRenderer renderer;
    private final JogoMusica musica;

    private final Set<Integer> teclasSeguradas = new HashSet<>();

    public InputHandler(JogoLogica logica, JogoRenderer renderer, JogoMusica musica) {
        this.logica = logica;
        this.renderer = renderer;
        this.musica = musica;
    }

    public void ativar(Scene scene) {
        scene.setOnKeyPressed(event -> {
            int lane = teclaParaLane(event.getCode());
            if (lane == -1) return;

            if (!teclasSeguradas.contains(lane)) {
                teclasSeguradas.add(lane);
                checarHit(lane); // TAP
            }
        });

        scene.setOnKeyReleased(event -> {
            int lane = teclaParaLane(event.getCode());
            if (lane == -1) return;
            teclasSeguradas.remove(lane); // libera a tecla
        });
    }

    private int teclaParaLane(javafx.scene.input.KeyCode code) {
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
                acerto = nota.getJulgamento() != Julgamento.ERRO;
                break;
            }
        }

        // pisca HitDot
        for (HitDot dot : renderer.getHitDots()) {
            if (dot.getLane() == lane) {
                dot.piscar(acerto);
                break;
            }
        }
    }

    public void atualizarHolds() {

        for (int lane : teclasSeguradas) {
            for (HitDot dot : renderer.getHitDots()) {
                if (dot.getLane() == lane) {
                    dot.manter(true); // brilho constante enquanto tecla pressionada
                }
            }

            for (Nota nota : logica.getNotasAtivas()) {
                if (nota.getLane() == lane && nota.isAtiva()) {
                    nota.segurar(musica.getTempoMusicaMs());
                }
            }
        }

        // reset dots não pressionados
        for (HitDot dot : renderer.getHitDots()) {
            if (!teclasSeguradas.contains(dot.getLane())) {
                dot.manter(false);
            }
        }
    }
}