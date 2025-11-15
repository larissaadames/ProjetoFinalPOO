package com.example.projetopoo;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.effect.GaussianBlur;


public class SeletorMusicaController {

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

    // id das músicas correspondentes aos cards
    private final String[] songIds = { "allstar", "numb", "bmtl" };

    private int index = 0;          // card selecionado
    private int scrollOffset = 0;   // offset vertical do placar (para scroll ↑/↓)
    private static final int PAGE_SIZE = 10;

    private Runnable onBack;                 // ESC
    private Consumer<Integer> onConfirm;     // ENTER → índice 0..2

    @FXML
    private void initialize() {
        cards = List.of(card1, card2, card3);
        covers = List.of(cover1, cover2, cover3);
        scoreBoxes = List.of(scoreBox1, scoreBox2, scoreBox3);

        applyHighlight();
        updateScoreboard();       // já mostra o placar da música inicial
        showBack(index);          // card inicial já começa mostrando o placar
        playEntranceAnimation();
        startTrianglePulse();
        Platform.runLater(() -> playPreview(index));
        startFireBreathing();
        applyFireSoftening();


        Platform.runLater(() -> {
            root.setFocusTraversable(true);
            root.requestFocus();
            root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        });
    }

    private void handleKey(KeyEvent e) {
        KeyCode c = e.getCode();
        switch (c) {
            case LEFT:
            case A:
                move(-1);
                e.consume();
                break;
            case RIGHT:
            case D:
                move(+1);
                e.consume();
                break;
            case UP:
            case W:
                scroll(-1);
                e.consume();
                break;
            case DOWN:
            case S:
                scroll(+1);
                e.consume();
                break;
            case ENTER:
            case SPACE:
                if (onConfirm != null) onConfirm.accept(index);
                e.consume();
                break;
            case ESCAPE:
                if (onBack != null) onBack.run();
                e.consume();
                break;
        }
    }

    private void move(int dir) {
        int old = index;
        index = Math.max(0, Math.min(cards.size() - 1, index + dir));

        if (index != old) {
            scrollOffset = 0;

            flipToFront(old);
            flipToBack(index);

            applyHighlight();
            animateBump(cards.get(index));

            stopPreview(() -> {});
            playPreview(index);  // 🔥 toca a música nova
        }
    }



    private void scroll(int dir) {
        // scroll do placar atual, se houver mais entradas que PAGE_SIZE
        String songId = songIds[index];
        List<ScoreEntry> scores = HighScoreManager.getInstance().getScores(songId);

        int maxOffset = Math.max(0, scores.size() - PAGE_SIZE);
        scrollOffset = Math.max(0, Math.min(maxOffset, scrollOffset + dir));
        updateScoreboard();
    }

    private void applyHighlight() {
        for (int i = 0; i < cards.size(); i++) {
            StackPane card = cards.get(i);
            boolean selected = (i == index);

            if (selected) {
                if (!card.getStyleClass().contains("selected")) {
                    card.getStyleClass().add("selected");
                }
                card.setScaleX(1.12);
                card.setScaleY(1.12);
                card.setOpacity(1.0);
            } else {
                card.getStyleClass().remove("selected");
                card.setScaleX(0.92);
                card.setScaleY(0.92);
                card.setOpacity(0.75);
            }


        }
    }

    /** Atualiza o placar do card atualmente selecionado */
    private void updateScoreboard() {
        String songId = songIds[index];
        VBox box = scoreBoxes.get(index);

        // mantém o primeiro filho (o título) e remove o resto
        List<Node> children = new ArrayList<>(box.getChildren());
        Node titleNode = children.get(0); // Label "X - Top Scores"
        box.getChildren().clear();
        box.getChildren().add(titleNode);

        List<ScoreEntry> scoresAll = HighScoreManager.getInstance().getScores(songId);

        int start = Math.min(scrollOffset, Math.max(0, scoresAll.size() - 1));
        int end = Math.min(start + PAGE_SIZE, scoresAll.size());

        for (int i = start; i < end; i++) {
            ScoreEntry se = scoresAll.get(i);
            int rank = i + 1;
            String text = String.format("%2d. %-10s  %6d", rank, se.getPlayerName(), se.getScore());

            Label lbl = new Label(text);
            lbl.getStyleClass().add("score-entry");

            box.getChildren().add(lbl);
        }

        if (scoresAll.isEmpty()) {
            Label lbl = new Label("Nenhum score ainda. Seja o primeiro!");
            lbl.getStyleClass().add("score-entry");
            box.getChildren().add(lbl);
        }
    }

    // ---------- animações (como já tínhamos) ----------

    private void animateBump(StackPane node) {
        double baseX = node.getScaleX();
        double baseY = node.getScaleY();

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.scaleXProperty(), baseX),
                        new KeyValue(node.scaleYProperty(), baseY)),
                new KeyFrame(Duration.millis(110),
                        new KeyValue(node.scaleXProperty(), baseX + 0.05),
                        new KeyValue(node.scaleYProperty(), baseY + 0.05)),
                new KeyFrame(Duration.millis(220),
                        new KeyValue(node.scaleXProperty(), baseX),
                        new KeyValue(node.scaleYProperty(), baseY))
        );
        tl.play();
    }

    private void playEntranceAnimation() {
        double initialOffset = 80;
        double durationMs = 850;
        long delayStep = 220L;

        for (int i = 0; i < cards.size(); i++) {
            StackPane card = cards.get(i);

            double finalOpacity = card.getOpacity();
            card.setOpacity(0.0);
            card.setTranslateY(initialOffset);

            Timeline tl = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(card.opacityProperty(), 0.0),
                            new KeyValue(card.translateYProperty(), initialOffset)),
                    new KeyFrame(Duration.millis(durationMs),
                            new KeyValue(card.opacityProperty(), finalOpacity, Interpolator.EASE_OUT),
                            new KeyValue(card.translateYProperty(), 0, Interpolator.EASE_OUT))
            );

            tl.setDelay(Duration.millis(delayStep * i));
            tl.play();
        }
    }

    private void startTrianglePulse() {
        Platform.runLater(() -> {
            animateArrows(arrowTop1, arrowBottom1);
            animateArrows(arrowTop2, arrowBottom2);
            animateArrows(arrowTop3, arrowBottom3);
        });
    }

    private void animateArrows(Polygon top, Polygon bottom) {
        double topBaseY = top.getTranslateY();
        double bottomBaseY = bottom.getTranslateY();

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(top.translateYProperty(), topBaseY),
                        new KeyValue(bottom.translateYProperty(), bottomBaseY)),
                new KeyFrame(Duration.seconds(0.6),
                        new KeyValue(top.translateYProperty(), topBaseY + 12),
                        new KeyValue(bottom.translateYProperty(), bottomBaseY - 12)),
                new KeyFrame(Duration.seconds(1.2),
                        new KeyValue(top.translateYProperty(), topBaseY),
                        new KeyValue(bottom.translateYProperty(), bottomBaseY))
        );

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.setAutoReverse(true);
        tl.play();
    }

    private void showFront(int i) {
        covers.get(i).setVisible(true);
        scoreBoxes.get(i).setVisible(false);
        scoreBoxes.get(i).setOpacity(0.0);
    }

    private void showBack(int i) {
        covers.get(i).setVisible(false);
        scoreBoxes.get(i).setVisible(true);
        scoreBoxes.get(i).setOpacity(1.0);
    }

    private void flipToBack(int i) {
        StackPane card = cards.get(i);
        card.setRotationAxis(Rotate.Y_AXIS);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(card.rotateProperty(), 0, Interpolator.EASE_IN)),
                new KeyFrame(Duration.millis(150),
                        new KeyValue(card.rotateProperty(), 90, Interpolator.EASE_IN),
                        new KeyValue(card.opacityProperty(), card.getOpacity(), Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(card.rotateProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(card.opacityProperty(), card.getOpacity(), Interpolator.LINEAR))
        );

        // no meio do flip (em ~150ms), troca capa → placar
        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(150),
                        e -> {
                            updateScoreboard();  // garante placar atualizado
                            showBack(i);
                        })
        );

        tl.play();
    }

    private void flipToFront(int i) {
        StackPane card = cards.get(i);
        card.setRotationAxis(Rotate.Y_AXIS);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(card.rotateProperty(), 0, Interpolator.EASE_IN)),
                new KeyFrame(Duration.millis(150),
                        new KeyValue(card.rotateProperty(), 90, Interpolator.EASE_IN),
                        new KeyValue(card.opacityProperty(), card.getOpacity(), Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(card.rotateProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(card.opacityProperty(), card.getOpacity(), Interpolator.LINEAR))
        );

        // no meio: troca placar → capa
        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(150),
                        e -> showFront(i))
        );

        tl.play();
    }

    private final String[] previewPaths = {
            "/musics/allstar.mp3",
            "/musics/numb.mp3",
            "/musics/bmtl.mp3"
    };

    // evita criar vários players
    private boolean previewLoading = false;

    private void playPreview(int index) {

        // para animação limpa ao trocar de música
        stopPreview(() -> {

            try {
                String path = previewPaths[index];
                Media media = new Media(getClass().getResource(path).toExternalForm());
                previewPlayer = new MediaPlayer(media);

                previewPlayer.setStartTime(Duration.seconds(20));
                previewPlayer.setStopTime(Duration.seconds(30));
                previewPlayer.setCycleCount(MediaPlayer.INDEFINITE);

                // 🔥 Quando o áudio estiver pronto pra tocar…
                previewPlayer.setOnReady(() -> {

                    // volume inicial
                    previewPlayer.setVolume(0);

                    // CONFIGURAÇÃO DO SPECTRUM (tem que estar AQUI)
                    previewPlayer.setAudioSpectrumInterval(0.03);   // ~33 FPS
                    previewPlayer.setAudioSpectrumNumBands(32);     // 32 bandas
                    previewPlayer.setAudioSpectrumListener((t, d, mags, phases) -> {

                        //System.out.println("MAG0 = " + mags[0]);  //Print pra debug, dependendo do valor tem que mudar o mp3

                        // energia média do áudio
                        double energy = 0;
                        for (double m : mags) {
                            energy += (m + 60); // normaliza -60dB → 0
                        }
                        energy /= mags.length;

                        // efeitos visuais
                        double scale = 1.0 + (energy / 120.0);
                        double opacity = 0.30 + (energy / 350.0);

                        Platform.runLater(() -> {
                            bgFire.setScaleX(scale);
                            bgFire.setScaleY(scale);
                            bgFire.setOpacity(opacity);
                        });
                    });

                    // tocar
                    previewPlayer.play();

                    // fade in suave
                    Timeline fadeIn = new Timeline(
                            new KeyFrame(Duration.seconds(0.0),
                                    new KeyValue(previewPlayer.volumeProperty(), 0.0)),
                            new KeyFrame(Duration.seconds(1.0),
                                    new KeyValue(previewPlayer.volumeProperty(), 0.25))
                    );
                    fadeIn.play();
                });

            } catch (Exception e) {
                System.err.println("Erro ao tocar preview: " + e.getMessage());
            }
        });
    }




    private void stopPreview(Runnable onStopped) {
        if (previewPlayer != null) {

            MediaPlayer old = previewPlayer;
            previewPlayer = null;

            // fade out
            Timeline fadeOut = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(old.volumeProperty(), 0.0))
            );

            fadeOut.setOnFinished(e -> {
                try { old.stop(); } catch (Exception ignored) {}
                try { old.dispose(); } catch (Exception ignored) {}

                // reset do fogo
                Platform.runLater(() -> {
                    bgFire.setScaleX(1);
                    bgFire.setScaleY(1);
                    bgFire.setOpacity(0.35);
                });

                onStopped.run();
            });

            fadeOut.play();

        } else {
            onStopped.run();
        }
    }

    private void startFireBreathing() {

        if (bgFire == null) return;

        Timeline slowBreath = new Timeline(
                new KeyFrame(Duration.seconds(0.0),
                        new KeyValue(bgFire.scaleXProperty(), 1.0),
                        new KeyValue(bgFire.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3.0),
                        new KeyValue(bgFire.scaleXProperty(), 1.08),
                        new KeyValue(bgFire.scaleYProperty(), 1.08)),
                new KeyFrame(Duration.seconds(6.0),
                        new KeyValue(bgFire.scaleXProperty(), 1.0),
                        new KeyValue(bgFire.scaleYProperty(), 1.0))
        );

        slowBreath.setCycleCount(Timeline.INDEFINITE);
        slowBreath.setAutoReverse(true);
        slowBreath.play();
    }


    private void applyFireSoftening() {
        if (bgFire == null) return;

        bgFire.setOpacity(0.28);        // antes 0.35
        bgFire.setEffect(new GaussianBlur(6)); // esfumaça levemente
    }




    // --------- callbacks para Main/MenuScene ---------

    public void setOnBack(Runnable r) {
        this.onBack = () -> {
            stopPreview(() -> {});
            r.run();
        };
    }


    public void setOnConfirm(Consumer<Integer> c) {
        this.onConfirm = (idx) -> {
            stopPreview(() -> {});
            c.accept(idx);
        };
    }


    public void setInitialIndex(int i) {
        index = Math.max(0, Math.min(cards.size() - 1, i));
        scrollOffset = 0;
        applyHighlight();
        updateScoreboard();
    }
}
