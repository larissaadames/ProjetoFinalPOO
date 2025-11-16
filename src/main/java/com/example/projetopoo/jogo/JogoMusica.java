package com.example.projetopoo.jogo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class JogoMusica {
    private final MediaPlayer player;

    public JogoMusica(String caminhoMusica) {
        Media media = new Media(Objects.requireNonNull(getClass().getResource(caminhoMusica)).toString());
        player = new MediaPlayer(media);
    }

    public void play() {
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public double getTempoMusicaMs() {
        return player.getCurrentTime().toMillis();
    }

}
