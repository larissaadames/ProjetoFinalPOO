package com.example.projetopoo.jogo;

public class NotaHold extends Nota {

    private final double duracaoMs; // duração da nota HOLD
    private boolean segurando = false; // se o jogador está segurando a nota
    private boolean acertoRegistrado = false; // se já registrou o acerto total

    public NotaHold(int lane, double momentoHit, double duracaoMs) {
        super(lane, momentoHit);
        this.duracaoMs = duracaoMs;
        this.setTipo(NotaTipo.HOLD);

    }

    @Override
    public NotaSprite criarSprite() {
        return null;
    }

    // tenta iniciar o hold
    @Override
    public void tentaHit(double momentoAtualMusicaMs) {
        if (!isAtiva()) return;

        double diff = momentoAtualMusicaMs - momentoHit;

        // só permite iniciar o hold se estiver dentro da janela de hit
        if (Math.abs(diff) <= windowHitRuim) {
            segurando = true;
            estado = NotaEstado.ACERTO; // marca como “acerto” temporário
        } else if (diff > windowHitRuim) {
            estado = NotaEstado.ERROU;
            julgamento = Julgamento.ERRO;
        }
    }

    @Override
    public void atualizar(double deltaTime, double tempoMusicaMs) {
        // movimento vertical da nota
        double restante = momentoHit - tempoMusicaMs;
        double posY = Nota.HIT_LINE - (restante * SCROLL_SPEED);
        setY(posY);

        // verifica se o tempo da nota terminou
        if (tempoMusicaMs >= momentoHit + duracaoMs) {
            if (segurando) {
                // acerto perfeito se segurou até o final
                julgamento = Julgamento.PERFEITO;
            } else if (!acertoRegistrado) {
                // erro se soltou cedo
                julgamento = Julgamento.ERRO;
            }
            this.setAtiva(false);
            acertoRegistrado = true;
        }
    }

    // chamada pelo InputHandler enquanto tecla estiver pressionada
    public void segurar(double tempoMusicaMs) {
        if (!isAtiva()) return;

        // se soltou antes do final
        if (tempoMusicaMs > momentoHit + duracaoMs) {
            segurando = false;
            this.setAtiva(false);

            if (!acertoRegistrado) {
                estado = NotaEstado.ERROU;
                julgamento = Julgamento.ERRO;
            }
        }
    }

    public boolean isSegurando() {
        return segurando;
    }

    public double getDuracaoMs() {
        return this.duracaoMs;
    }


}