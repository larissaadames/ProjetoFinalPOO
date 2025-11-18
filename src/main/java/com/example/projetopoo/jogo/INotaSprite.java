package com.example.projetopoo.jogo;

import javafx.scene.Node;

public interface INotaSprite {
    void atualizar(double tempoMusicaMs);
    Node getNode();
    Nota getNota();
}