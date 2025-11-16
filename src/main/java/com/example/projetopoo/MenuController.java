package com.example.projetopoo;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import java.util.Random;

public class MenuController {

    @FXML private AnchorPane root;
    @FXML private Label title;
    @FXML private VBox menuBox;
    @FXML private Pane lightningLayer;

    private int index = 0;
    private Label[] items;

    private final Random rng = new Random();

    @FXML
    private void initialize() {

        // pega as labels do menu
        items = menuBox.getChildren().stream()
                .map(n -> (Label) n)
                .toArray(Label[]::new);

        highlightItem();

        playEntranceAnim();
        playGuitarSFX();

        Platform.runLater(() -> root.requestFocus());

        // raios automáticos
        iniciarRaios();

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {

                case DOWN:
                case S:
                    index = (index + 1) % items.length;
                    highlightItem();
                    break;

                case UP:
                case W:
                    index = (index - 1 + items.length) % items.length;
                    highlightItem();
                    break;

                case ENTER:
                case SPACE:
                    activate(index);
                    break;

                case ESCAPE:
                    ControladorCenas.irParaMenu();
                    break;

                default:
                    break;
            }
        });

    }

    /** animação de entrada */
    private void playEntranceAnim() {
        title.setOpacity(0);
        menuBox.setOpacity(0);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(title.opacityProperty(), 0),
                        new KeyValue(menuBox.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(title.opacityProperty(), 1),
                        new KeyValue(menuBox.opacityProperty(), 1))
        );
        tl.play();
    }

    /** som inicial */
    private void playGuitarSFX() {
        try {
            Media m = new Media(getClass().getResource("/sfx/guitar_hit.mp3").toExternalForm());
            MediaPlayer mp = new MediaPlayer(m);
            mp.setVolume(0.55);
            mp.play();
        } catch (Exception e) {
            System.out.println("SFX erro: " + e.getMessage());
        }
    }

    /** seta + borda elétrica dourada */
    private void highlightItem() {

        for (int i = 0; i < items.length; i++) {
            Label lbl = items[i];

            if (i == index) {
                lbl.getStyleClass().add("menu-selected");
                animateElectricBorder(lbl);
                lbl.setText("> " + lbl.getText().replace("> ", "").replace(" <", "") + " <");
            } else {
                lbl.getStyleClass().remove("menu-selected");
                lbl.setStyle("-fx-border-color: transparent;");
                lbl.setText(lbl.getText().replace("> ", "").replace(" <", ""));
            }
        }
    }

    /** borda elétrica dourada */
    private void animateElectricBorder(Label lbl) {

        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lbl.styleProperty(),
                        "-fx-border-color: #ffd86b; -fx-border-width: 4")),
                new KeyFrame(Duration.millis(70), new KeyValue(lbl.styleProperty(),
                        "-fx-border-color: #fff9c4; -fx-border-width: 4")),
                new KeyFrame(Duration.millis(140), new KeyValue(lbl.styleProperty(),
                        "-fx-border-color: #ffef9a; -fx-border-width: 4"))
        );
        t.setCycleCount(Animation.INDEFINITE);
        t.setAutoReverse(true);
        t.play();

        lbl.setUserData(t);
    }

    /** abrir jogo / cenas */
    private void activate(int option) {
        switch (option) {
            case 0 -> ControladorCenas.irParaSelecaoMusicas();
            case 1 -> {}
            case 2 -> System.exit(0);
        }
    }

    // ==========================
    // 🔥 EFEITO DE RAIOS NO FUNDO
    // ==========================

    private void iniciarRaios() {
        Timeline loop = new Timeline(
                new KeyFrame(Duration.seconds(0.4), e -> {
                    if (rng.nextInt(3) == 0) gerarRaio();
                })
        );

        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    private void gerarRaio() {
        lightningLayer.getChildren().clear();

        double startX = rng.nextInt(1920);
        double endX = startX + rng.nextInt(400) - 200;

        Polyline raio = new Polyline();
        raio.setStrokeWidth(4);
        raio.setStroke(javafx.scene.paint.Color.WHITE);

        for (int i = 0; i < 8; i++) {
            double x = startX + (endX - startX) * (i / 7.0) + rng.nextInt(120) - 60;
            double y = i * 140;
            raio.getPoints().addAll(x, y);
        }

        lightningLayer.getChildren().add(raio);

        Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(raio.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(200), new KeyValue(raio.opacityProperty(), 0))
        );
        fade.setOnFinished(e -> lightningLayer.getChildren().clear());
        fade.play();
    }
}
