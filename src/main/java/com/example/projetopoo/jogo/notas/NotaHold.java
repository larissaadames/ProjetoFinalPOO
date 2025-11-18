package com.example.projetopoo.jogo.notas;

import com.example.projetopoo.jogo.render.INotaSprite;
import com.example.projetopoo.jogo.render.JogoRenderer;
import com.example.projetopoo.jogo.chart.NotaTipo;
import com.example.projetopoo.jogo.core.Layout;
import com.example.projetopoo.jogo.logica.Julgamento;

public class NotaHold extends Nota {

    private final double duracaoMs;
    private boolean segurando = false;
    private boolean acertoRegistrado = false;
    private static final double INTERVALO_TICK_MS = 100;
    private double ultimoTickMs = 0;

    private boolean recebeuInput = false;

    public NotaHold(int lane, double momentoHit, double duracaoMs) {
        super(lane, momentoHit);
        this.duracaoMs = duracaoMs;
        this.setTipo(NotaTipo.HOLD);
    }

    @Override
    public void tentaHit(double momentoAtualMusicaMs) {
        if (!isAtiva()) return;

        double diff = momentoAtualMusicaMs - momentoHit;

        if (Math.abs(diff) <= windowHitRuim) {
            segurando = true;
            estado = NotaEstado.SEGURANDO;

            // Marca que recebeu input neste frame exato
            recebeuInput = true;
        } else if (diff > windowHitRuim) {
            estado = NotaEstado.ERROU;
            julgamento = Julgamento.ERRO;
        }
    }

    @Override
    public void segurar(double tempoMusicaMs) {
        this.recebeuInput = true;
    }

    @Override
    public void atualizar(double deltaTime, double tempoMusicaMs) {

        if (segurando && !recebeuInput) {
            segurando = false;

            if (!acertoRegistrado) {
                julgamento = Julgamento.OTIMO;
                setAtiva(false);
            }
        }

        recebeuInput = false;

        if (segurando) {
            setY(Layout.HIT_LINE);
        } else {
            double restante = momentoHit - tempoMusicaMs;
            double posY = Layout.HIT_LINE - (restante * SCROLL_SPEED);
            setY(posY);
        }

        if (tempoMusicaMs >= momentoHit + duracaoMs) {
            if (segurando) {
                julgamento = Julgamento.PERFEITO;
                estado = NotaEstado.ACERTO;
            } else if (!acertoRegistrado) {
                julgamento = Julgamento.ERRO;
                estado = NotaEstado.ERROU;
            }
            this.setAtiva(false);
            acertoRegistrado = true;
        }
    }

    @Override
    public boolean deveDespawnar(double tempoMusicaMs) {
        if (estado == NotaEstado.ACERTO || estado == NotaEstado.ERROU) return true;
        if (!isAtiva()) return true;
        if (tempoMusicaMs > momentoHit + duracaoMs + windowHitRuim) return true;
        return false;
    }

    @Override
    public boolean checarTick(double tempoMusicaMs) {
        if (segurando && (tempoMusicaMs - ultimoTickMs >= INTERVALO_TICK_MS)) {

            ultimoTickMs += INTERVALO_TICK_MS;

            return true;
        }
        return false;
    }

    public double getDuracaoMs() { return this.duracaoMs; }

    @Override
    public INotaSprite obterSprite(JogoRenderer renderer) {
        return renderer.obterHoldDaPool(this);
    }
}