package com.example.projetopoo.jogo;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.List;

public class InputHandler {

    private final JogoLogica logica;
    private final JogoRenderer renderer;
    private final JogoMusica musica;

    // OTIMIZAÇÃO: Array primitivo em vez de HashSet<Integer>.
    // Index 0 não usado, índices 1-5 representam as lanes.
    private final boolean[] teclasPressionadas = new boolean[6];

    public InputHandler(JogoLogica logica, JogoRenderer renderer, JogoMusica musica) {
        this.logica = logica;
        this.renderer = renderer;
        this.musica = musica;
    }

    public void ativar(Scene scene) {
        scene.setOnKeyPressed(event -> {
            int lane = teclaParaLane(event.getCode());
            if (lane == -1) return;

            // Se a tecla já não estava pressionada (evita spam do evento)
            if (!teclasPressionadas[lane]) {
                teclasPressionadas[lane] = true;
                checarHit(lane); // TAP
            }
        });

        scene.setOnKeyReleased(event -> {
            int lane = teclaParaLane(event.getCode());
            if (lane == -1) return;

            teclasPressionadas[lane] = false; // Libera a tecla
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
                // Pequena otimização: Se acertou, paramos de procurar
                if (nota.getEstado() != NotaEstado.PENDENTE && nota.getEstado() != NotaEstado.ERROU) {
                    acerto = true;
                    break;
                }
            }
        }

        // Feedback visual nos HitDots
        List<HitDot> dots = renderer.getHitDots();
        // Otimização: Acesso direto por índice se possível, ou loop simples
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

        // 1. Atualiza efeitos visuais dos Dots
        List<HitDot> dots = renderer.getHitDots();
        for (int i = 0; i < dots.size(); i++) {
            HitDot dot = dots.get(i);
            // Verifica o array de boolean diretamente
            dot.manter(teclasPressionadas[dot.getLane()]);
        }

        // 2. Processa a lógica de Segurar (Hold)
        for (Nota nota : logica.getNotasAtivas()) {
            if (nota.isAtiva() && teclasPressionadas[nota.getLane()]) {
                nota.segurar(tempoAtual);
            }
        }
    }
}