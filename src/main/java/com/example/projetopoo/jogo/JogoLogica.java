package com.example.projetopoo.jogo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JogoLogica {
    private final List<JogoChartNota> chartNotas;
    private final List<Nota> notasAtivas = new ArrayList<>();
    private final List<Nota> notasFinalizadasNesteFrame = new ArrayList<>();
    private int proxima = 0;

    public JogoLogica(JogoChart chart) {
        this.chartNotas = chart.getNotas();
    }

    public void atualizar(double deltaTime, double tempoMusicaMs) {
//        System.out.println("tempoMusicaMs = " + tempoMusicaMs);
        notasFinalizadasNesteFrame.clear();
        checarSpawn(tempoMusicaMs);

        Iterator<Nota> iterator = notasAtivas.iterator(); //iterator serve pra nao dar erro quando remover a nota enquanto itera


        // percorre a lista
        while (iterator.hasNext()) {
            Nota nota = iterator.next();

            nota.atualizaErro(tempoMusicaMs);

            nota.atualizar(deltaTime, tempoMusicaMs);
            if (nota.deveDespawnar(tempoMusicaMs)) {

                if (nota.getJulgamento() != null) {
                    notasFinalizadasNesteFrame.add(nota);
                }

                nota.setAtiva(false);
                iterator.remove();
            }
        }
    }

    public List<Nota> getNotasFinalizadasNesteFrame() {
        return notasFinalizadasNesteFrame;
    }

    private void checarSpawn(double tempoMusicaMs){

        // enquanto nao tiver acabado a chart, e ja tiver dado o tempo das notas spawnarem
        while (proxima < chartNotas.size() && chartNotas.get(proxima).getTempoMs() - Nota.SPAWN_OFFSET_MS <= tempoMusicaMs) {
            JogoChartNota raw = chartNotas.get(proxima);

            Nota nota;

            switch (raw.getTipo()) {
                case TAP:
                    nota = new NotaTap(raw.getLane(), raw.getTempoMs());
                    notasAtivas.add(nota);
                    break;

                case HOLD:
                    nota = new NotaHold(raw.getLane(), raw.getTempoMs(), raw.getDuracaoMs());
                    notasAtivas.add(nota);
                    break;
            }

            proxima++;
        }
    }

    public List<Nota> getNotasAtivas() {
        return notasAtivas;
    }
}
