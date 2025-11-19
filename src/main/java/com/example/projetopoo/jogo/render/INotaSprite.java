package com.example.projetopoo.jogo.render;

import com.example.projetopoo.jogo.notas.Nota;
import com.example.projetopoo.jogo.notas.NotaHold;
import com.example.projetopoo.jogo.notas.NotaTap;
import javafx.scene.Node;

public interface INotaSprite {
    void atualizar(double tempoMusicaMs);
    Node getNode();
    Nota getNota();

    void reusar(NotaTap nota);
    void reusar(NotaHold nota);
    void devolverPara(JogoRenderer renderer);

}