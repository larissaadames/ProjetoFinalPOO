package com.example.projetopoo.jogo;

import java.util.ArrayList;

public class JogoLogica {
    DeltaTime deltatime = new DeltaTime();
    ArrayList<Nota> notas = new ArrayList<>();

    public void atualizar() {
        for (Nota nota : notas) {
            nota.atualizar(deltatime.getDeltatime());
        }
    }
}
