package com.example.projetopoo.jogo;

public class JogoChartNota {
    private int lane;
    private double tempoMs;
    private double duracaoMs;
    private NotaTipo tipo;

    public JogoChartNota(){} // isso é vazio pq o Gson poe o Json ali dentro certinho

    public int getLane() {return lane;}

    public double getTempoMs() {return tempoMs;}

    public NotaTipo getTipo() {return tipo;}

    public double getDuracaoMs() {return duracaoMs;}
}
