package com.example.projetopoo.jogo;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Objects;

public class CarregaJogoChart {

    private final String caminhoMusica;

    public CarregaJogoChart(String caminhoMusica) {
        this.caminhoMusica = caminhoMusica;
    }

    public static JogoChart carregar(String caminho) throws IOException {
        URL url = Objects.requireNonNull(CarregaJogoChart.class.getResource(caminho));
        String json = new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8);


        Gson gson = new Gson();
        JogoChart chart = gson.fromJson(json, JogoChart.class);


        chart.getNotas().sort(Comparator.comparingDouble(JogoChartNota::getTempoMs)); // ta ordenando a lista em crescente se o json n tiver

        return chart;
    }
}
