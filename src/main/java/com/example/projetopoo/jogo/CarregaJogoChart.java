package com.example.projetopoo.jogo;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

public class CarregaJogoChart {

    private final String caminhoMusica;

    public CarregaJogoChart(String caminhoMusica) {
        this.caminhoMusica = caminhoMusica;
    }

    public static JogoChart carregar(String caminho) throws IOException {
        String json = Files.readString(Paths.get(caminho));

        Gson gson = new Gson();
        JogoChart chart = gson.fromJson(json, JogoChart.class);

        chart.getNotas().sort(Comparator.comparingDouble(JogoChartNota::getTempoMs)); // ta ordenando a lista em crescente se o json n tiver

        return chart;
    }
}
