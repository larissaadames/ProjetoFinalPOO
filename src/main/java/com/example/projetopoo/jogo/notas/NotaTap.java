package com.example.projetopoo.jogo.notas;

import com.example.projetopoo.jogo.render.INotaSprite;
import com.example.projetopoo.jogo.render.JogoRenderer;
import com.example.projetopoo.jogo.chart.NotaTipo;
import com.example.projetopoo.jogo.core.Layout;

public class NotaTap extends Nota {

    public NotaTap(int lane, double momentoHit) {
        super(lane, momentoHit);
        this.setTipo(NotaTipo.TAP);
    }

    @Override
    public void tentaHit(double momentoAtualMusicaMs) {
        double diff = Math.abs(momentoAtualMusicaMs - momentoHit);

        if (!isAtiva() || diff > windowHitRuim) return;

        onHit(momentoAtualMusicaMs);
    }

    @Override
    public void atualizar(double deltaTime, double tempoMusicaMs) {

        double restante = momentoHit - tempoMusicaMs;

        double posY = Layout.HIT_LINE - (restante * SCROLL_SPEED);

        this.setY(posY);
    }

    @Override
    public void segurar(double tempoMusicaMs) {
        return;
    }

    @Override
    public INotaSprite obterSprite(JogoRenderer renderer) {
        return renderer.obterTapDaPool(this);
    }

}
