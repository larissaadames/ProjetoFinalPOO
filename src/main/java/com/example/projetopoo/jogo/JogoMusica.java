package com.example.projetopoo.jogo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.lang.Runnable;

import java.util.Objects;

public class JogoMusica {
    private final MediaPlayer player;
    private Runnable acaoFimMusica;

    public JogoMusica(String caminhoMusica) {
        Media media = new Media(Objects.requireNonNull(getClass().getResource(caminhoMusica)).toExternalForm());
        player = new MediaPlayer(media);
        player.setVolume(0.25);

        player.setOnEndOfMedia(() -> acaoFimMusica.run());

    }

    public void iniciarComOffset(double tempoMs) {
        player.setStartTime(Duration.millis(tempoMs));
        player.play();
    }

    public void setAcaoFimMusica(Runnable acao) {
        this.acaoFimMusica = acao;
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
