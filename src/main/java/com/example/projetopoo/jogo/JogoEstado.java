package com.example.projetopoo.jogo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class JogoEstado {
    // property do javafx que faz a tela atualizar sozinha quando o valor muda
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);
    private final IntegerProperty multiplicador = new SimpleIntegerProperty(1);

    public JogoEstado() {
        combo.addListener((obs, valorAntigo, valorNovo) -> {
            atualizarMultiplicador(valorNovo.intValue());
        });
    }

    private void atualizarMultiplicador(int comboAtual) {
        int novoMult = 1;

        if (comboAtual >= 40) novoMult = 5;
        else if (comboAtual >= 30) novoMult = 4;
        else if (comboAtual >= 20) novoMult = 3;
        else if (comboAtual >= 10) novoMult = 2;

        if (novoMult != multiplicador.get()) {
            multiplicador.set(novoMult);
        }
    }

    public void registrarHit(Julgamento julgamento) {
        int pontosBase = 0;
        boolean quebraCombo = false;

        switch (julgamento) {
            case PERFEITO -> {
                pontosBase = 300;
            }
            case OTIMO -> pontosBase = 100;
            case RUIM -> {
                pontosBase = 50;
            }
            case ERRO -> quebraCombo = true;
        }

        if (quebraCombo) {
            combo.set(0);
        } else {

            int pontosFinais = pontosBase * multiplicador.get();
            score.set(score.get() + pontosFinais);

            combo.set(combo.get() + 1);
        }
    }

    public void adicionarPontosTick() {
        score.set(score.get() + (20 * multiplicador.get()));
    }

    public void registrarMiss() {
        combo.set(0);
    }

    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty comboProperty() { return combo; }
    public IntegerProperty multiplicadorProperty() { return multiplicador; }
}