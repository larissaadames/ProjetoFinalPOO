package com.example.projetopoo;

import com.example.projetopoo.exceptions.SongNotFoundException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.scene.effect.GaussianBlur;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SeletorMusicaController extends OrganizadorCenas {

    private MediaPlayer previewPlayer;

    @FXML private AnchorPane root;
    @FXML private ImageView bgFire;

    @FXML private StackPane card1;
    @FXML private StackPane card2;
    @FXML private StackPane card3;

    @FXML private ImageView cover1;
    @FXML private ImageView cover2;
    @FXML private ImageView cover3;

    @FXML private VBox scoreBox1;
    @FXML private VBox scoreBox2;
    @FXML private VBox scoreBox3;

    @FXML private Polygon arrowTop1;
    @FXML private Polygon arrowBottom1;
    @FXML private Polygon arrowTop2;
    @FXML private Polygon arrowBottom2;
    @FXML private Polygon arrowTop3;
    @FXML private Polygon arrowBottom3;

    private List<StackPane> cards;
    private List<ImageView> covers;
    private List<VBox> scoreBoxes;
    private List<Polygon[]> arrows;

    private final String[] songIds = { "allstar", "numb", "bmtl" };
    private final String[] previewPaths = {
            "/musics/allstar.mp3",
            "/musics/numb.mp3",
            "/musics/bmtl.mp3"
    };

    private int index = 0;

    private Runnable onBack;
    private Consumer<Integer> onConfirm;

    @FXML
    private void initialize() {

        cards = List.of(card1, card2, card3);
        covers = List.of(cover1, cover2, cover3);
        scoreBoxes = List.of(scoreBox1, scoreBox2, scoreBox3);
        arrows = List.of(
                new Polygon[]{arrowTop1, arrowBottom1},
                new Polygon[]{arrowTop2, arrowBottom2},
                new Polygon[]{arrowTop3, arrowBottom3}
        );

        for (int i = 0; i < 3; i++) {
            showFront(i);
        }

        showBack(index);
        applyHighlight();
        updateScoreboard();

        cards.get(index).getStyleClass().add("selected");

        startTrianglePulse();
        startFireBreathing();
        applyFireSoftening();
        playEntranceAnimation();

        Platform.runLater(() -> {
            root.requestFocus();
            root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
            playPreview(index);
        });
    }

    // Implementa o contrato da classe base para limpeza de mídia
    @Override
    public void exitSceneCleanup() {
        stopPreview(() -> {});
    }

    private void handleKey(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT, A -> move(-1);
            case RIGHT, D -> move(+1);
            case ENTER, SPACE -> {
                validarMusica(index);
                if (onConfirm != null) onConfirm.accept(index);
            }
            case ESCAPE -> {
                if (onBack != null) onBack.run();
            }
        }
        e.consume();
    }


    private void move(int dir) {
        int old = index;
        index = Math.max(0, Math.min(cards.size() - 1, index + dir));

        if (index == old) return;

        cards.get(old).getStyleClass().remove("selected");

        flipToFront(old);
        flipToBack(index);

        applyHighlight();

        cards.get(index).getStyleClass().add("selected");

        animateBump(cards.get(index));

        stopPreview(() -> playPreview(index));
    }


    private void applyHighlight() {
        for (int i = 0; i < cards.size(); i++) {
            StackPane card = cards.get(i);
            boolean selected = (i == index);

            card.setScaleX(selected ? 1.12 : 0.92);
            card.setScaleY(selected ? 1.12 : 0.92);
            card.setOpacity(selected ? 1.0 : 0.75);

            Rectangle cursor = (Rectangle) card.lookup("#cursor" + (i + 1));
            if (cursor != null) cursor.setStrokeWidth(selected ? 6 : 0);

            Polygon top = arrows.get(i)[0];
            Polygon bot = arrows.get(i)[1];

            top.setOpacity(selected ? 1 : 0);
            bot.setOpacity(selected ? 1 : 0);
        }
    }


    private void updateScoreboard() {
        validarMusica(index);

        VBox box = scoreBoxes.get(index);

        List<Node> children = new ArrayList<>(box.getChildren());
        Node titleNode = children.get(0);

        box.getChildren().clear();
        box.getChildren().add(titleNode);

        List<ScoreEntry> scores = HighScoreManager.getInstance().getScores(songIds[index]);

        if (scores.isEmpty()) {
            Label empty = new Label("Nenhum score ainda. Seja o primeiro!");
            empty.getStyleClass().add("score-entry");
            box.getChildren().add(empty);
            return;
        }

        for (int i = 0; i < Math.min(10, scores.size()); i++) {
            ScoreEntry s = scores.get(i);

            Label lbl = new Label(String.format(
                    "%2d. %-10s %6d", i + 1, s.getPlayerName(), s.getScore()
            ));
            lbl.getStyleClass().add("score-entry");
            box.getChildren().add(lbl);
        }
    }


    private void flipToBack(int i) {
        StackPane card = cards.get(i);
        card.setRotationAxis(Rotate.Y_AXIS);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(card.rotateProperty(), 0)),
                new KeyFrame(Duration.millis(150),
                        e -> { updateScoreboard(); showBack(i); },
                        new KeyValue(card.rotateProperty(), 90)),
                new KeyFrame(Duration.millis(300), new KeyValue(card.rotateProperty(), 0))
        );
        tl.play();
    }


    private void flipToFront(int i) {
        StackPane card = cards.get(i);
        card.setRotationAxis(Rotate.Y_AXIS);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(card.rotateProperty(), 0)),
                new KeyFrame(Duration.millis(150),
                        e -> showFront(i),
                        new KeyValue(card.rotateProperty(), 90)),
                new KeyFrame(Duration.millis(300), new KeyValue(card.rotateProperty(), 0))
        );
        tl.play();
    }


    private void showFront(int i) {
        covers.get(i).setVisible(true);
        scoreBoxes.get(i).setVisible(false);
        scoreBoxes.get(i).setOpacity(1.0);

        Label title = (Label) cards.get(i).lookup("#title" + (i + 1));
        if (title != null) title.setVisible(true);
    }

    private void showBack(int i) {
        covers.get(i).setVisible(false);
        scoreBoxes.get(i).setVisible(true);
        scoreBoxes.get(i).setOpacity(1.0);

        Label title = (Label) cards.get(i).lookup("#title" + (i + 1));
        if (title != null) title.setVisible(false);

        scoreBoxes.get(i).toFront();
    }


    private void animateBump(StackPane card) {
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(card.scaleXProperty(), 1.12)),
                new KeyFrame(Duration.millis(130), new KeyValue(card.scaleXProperty(), 1.18)),
                new KeyFrame(Duration.millis(250), new KeyValue(card.scaleXProperty(), 1.12))
        );
        tl.play();
    }


    private void playEntranceAnimation() {
        double offset = 80;

        for (int i = 0; i < cards.size(); i++) {

            StackPane card = cards.get(i);
            card.setOpacity(0);
            card.setTranslateY(offset);

            Timeline tl = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(card.opacityProperty(), 0),
                            new KeyValue(card.translateYProperty(), offset)),
                    new KeyFrame(Duration.millis(900),
                            new KeyValue(card.opacityProperty(), 1),
                            new KeyValue(card.translateYProperty(), 0))
            );

            tl.setDelay(Duration.millis(i * 200));
            tl.play();
        }
    }


    private void startTrianglePulse() {
        for (Polygon[] pair : arrows) {
            Polygon top = pair[0];
            Polygon bottom = pair[1];

            Timeline tl = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(top.translateYProperty(), -370),
                            new KeyValue(bottom.translateYProperty(), 370)),
                    new KeyFrame(Duration.seconds(0.6),
                            new KeyValue(top.translateYProperty(), -358),
                            new KeyValue(bottom.translateYProperty(), 358)),
                    new KeyFrame(Duration.seconds(1.2),
                            new KeyValue(top.translateYProperty(), -370),
                            new KeyValue(bottom.translateYProperty(), 370))
            );
            tl.setCycleCount(Timeline.INDEFINITE);
            tl.play();
        }
    }


    private void startFireBreathing() {
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(bgFire.scaleXProperty(), 1),
                        new KeyValue(bgFire.scaleYProperty(), 1)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(bgFire.scaleXProperty(), 1.08),
                        new KeyValue(bgFire.scaleYProperty(), 1.08)),
                new KeyFrame(Duration.seconds(6),
                        new KeyValue(bgFire.scaleXProperty(), 1),
                        new KeyValue(bgFire.scaleYProperty(), 1))
        );
        tl.setAutoReverse(true);
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }


    private void applyFireSoftening() {
        bgFire.setOpacity(0.28);
        bgFire.setEffect(new GaussianBlur(6));
    }


    private void playPreview(int index) {
        validarMusica(index);

        stopPreview(() -> {
            try {
                String path = previewPaths[index];

                if (getClass().getResource(path) == null) {
                    throw new SongNotFoundException("Arquivo MP3 não encontrado: " + path);
                }

                Media media = new Media(
                        getClass().getResource(path).toExternalForm()
                );

                MediaPlayer mp = new MediaPlayer(media);
                previewPlayer = mp;

                mp.setStartTime(Duration.seconds(20));
                mp.setStopTime(Duration.seconds(30));
                mp.setCycleCount(MediaPlayer.INDEFINITE);

                mp.setOnReady(() -> {
                    mp.setVolume(0.0);

                    mp.setAudioSpectrumNumBands(32);
                    mp.setAudioSpectrumInterval(0.03);

                    mp.setAudioSpectrumListener((time, dur, mags, phases) -> {
                        double energy = 0;

                        for (double m : mags)
                            energy += (m + 60);

                        energy /= mags.length;

                        double scale = 1.0 + (energy / 140.0);
                        double opacity = 0.25 + (energy / 300.0);

                        Platform.runLater(() -> {
                            bgFire.setScaleX(scale);
                            bgFire.setScaleY(scale);
                            bgFire.setOpacity(opacity);
                        });
                    });

                    mp.play();

                    Timeline fade = new Timeline(
                            new KeyFrame(Duration.seconds(1),
                                    new KeyValue(mp.volumeProperty(), 0.25))
                    );
                    fade.play();
                });

            } catch (Exception e) {
                throw new SongNotFoundException("Erro ao tocar preview: " + e.getMessage());
            }
        });
    }


    private void stopPreview(Runnable after) {
        if (previewPlayer == null) {
            after.run();
            return;
        }

        MediaPlayer old = previewPlayer;
        previewPlayer = null;

        Timeline fade = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(old.volumeProperty(), 0))
        );

        fade.setOnFinished(ev -> {
            try { old.stop(); } catch (Exception ignore) {}
            try { old.dispose(); } catch (Exception ignore) {}

            Platform.runLater(() -> {
                bgFire.setScaleX(1);
                bgFire.setScaleY(1);
                bgFire.setOpacity(0.28);
            });

            after.run();
        });

        fade.play();
    }

    // Chama o cleanup da base e executa o callback.
    public void setOnBack(Runnable r) {
        this.onBack = () -> stopPreview(() -> {
            exitSceneCleanup();
            r.run();
        });
    }

    // Chama o cleanup da base e executa o callback.
    public void setOnConfirm(Consumer<Integer> c) {
        this.onConfirm = idx -> {
            validarMusica(idx);
            stopPreview(() -> {
                exitSceneCleanup();
                c.accept(idx);
            });
        };
    }

    public void setInitialIndex(int i) {
        index = Math.max(0, Math.min(cards.size() - 1, i));
        applyHighlight();
        updateScoreboard();
    }

    private void validarMusica(int idx) {
        if (idx < 0 || idx >= songIds.length)
            throw new SongNotFoundException("Índice inválido de música: " + idx);

        if (songIds[idx] == null || songIds[idx].isBlank())
            throw new SongNotFoundException("songId vazio para índice: " + idx);

        if (previewPaths[idx] == null)
            throw new SongNotFoundException("Preview de música ausente no índice: " + idx);
    }
}