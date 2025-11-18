package com.example.projetopoo.jogo.core;

import com.example.projetopoo.jogo.logica.Julgamento;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class JogoEstado {
    // property do javafx que faz a tela atualizar sozinha quando o valor muda
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);
    private final IntegerProperty multiplicador = new SimpleIntegerProperty(1);

    private int maxCombo = 0;
    private final IntegerProperty acertosPerfeito = new SimpleIntegerProperty(0);
    private final IntegerProperty acertosOtimo = new SimpleIntegerProperty(0);
    private final IntegerProperty acertosRuim = new SimpleIntegerProperty(0);
    private final IntegerProperty acertosErro = new SimpleIntegerProperty(0);

    public JogoEstado() {
        combo.addListener((obs, valorAntigo, valorNovo) -> {
            int comboAtual = valorNovo.intValue();

            if (comboAtual > maxCombo) {
                maxCombo = comboAtual;
            }

            atualizarMultiplicador(comboAtual);
        });
    }

    private void atualizarMultiplicador(int comboAtual) {
        int novoMult = 1;

        if (comboAtual >= 200) novoMult = 5;
        else if (comboAtual >= 100) novoMult = 4;
        else if (comboAtual >= 50) novoMult = 3;
        else if (comboAtual >= 25) novoMult = 2;

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
                acertosPerfeito.set(acertosPerfeito.get() + 1);
            }
            case OTIMO -> {
                pontosBase = 100;
                acertosOtimo.set(acertosOtimo.get() + 1);
            }
            case RUIM -> {
                pontosBase = 50;
                acertosRuim.set(acertosRuim.get() + 1);
            }
            case ERRO -> {
                quebraCombo = true;
                acertosErro.set(acertosErro.get() + 1);
            }
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

    public int getMaxCombo() { return maxCombo; }
    public int getAcertosPerfeito() { return acertosPerfeito.get(); }
    public int getAcertosOtimo() { return acertosOtimo.get(); }
    public int getAcertosRuim() { return acertosRuim.get(); }
    public int getAcertosErro() { return acertosErro.get(); }

    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty comboProperty() { return combo; }
    public IntegerProperty multiplicadorProperty() { return multiplicador; }
}