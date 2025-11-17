package com.example.projetopoo.jogo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.Objects;

public class JogoMusica {
    private final MediaPlayer player;

    public JogoMusica(String caminhoMusica) {
        Media media = new Media(Objects.requireNonNull(getClass().getResource(caminhoMusica)).toExternalForm());
        player = new MediaPlayer(media);
        player.setVolume(0.25);

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
