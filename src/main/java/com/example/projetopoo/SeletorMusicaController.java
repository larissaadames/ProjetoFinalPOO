package com.example.projetopoo;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;

public class SeletorMusicaController {

    @FXML private AnchorPane root;
    @FXML private StackPane card1;
    @FXML private StackPane card2;
    @FXML private StackPane card3;

    private List<StackPane> cards;
    private int index = 0;

    private Runnable onBack;                 // ESC
    private Consumer<Integer> onConfirm;     // ENTER → índice 0..2

    @FXML
    private void initialize() {
        // lista de cards
        cards = List.of(card1, card2, card3);

        // destaque inicial (card 0 = Shrek)
        applyHighlight();

        // garante foco e captura de teclas DEPOIS que a cena já estiver montada
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
            applyHighlight();
            animateBump(cards.get(index));
        }
    }

    private void applyHighlight() {
        for (int i = 0; i < cards.size(); i++) {
            StackPane card = cards.get(i);
            if (i == index) {
                if (!card.getStyleClass().contains("selected")) {
                    card.getStyleClass().add("selected");
                }
            } else {
                card.getStyleClass().remove("selected");
            }
        }
    }

    private void animateBump(StackPane node) {
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.scaleXProperty(), 1.0),
                        new KeyValue(node.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.millis(80),
                        new KeyValue(node.scaleXProperty(), 1.04),
                        new KeyValue(node.scaleYProperty(), 1.04)),
                new KeyFrame(Duration.millis(160),
                        new KeyValue(node.scaleXProperty(), 1.0),
                        new KeyValue(node.scaleYProperty(), 1.0))
        );
        tl.play();
    }

    // --------- callbacks para Main/MenuScene ---------

    /** Esc → voltar ao menu */
    public void setOnBack(Runnable r) { this.onBack = r; }

    /** Enter → confirmar seleção (índice 0..2) */
    public void setOnConfirm(Consumer<Integer> c) { this.onConfirm = c; }

    /** opcional: definir card inicial antes de mostrar a tela */
    public void setInitialIndex(int i) {
        index = Math.max(0, Math.min(cards.size() - 1, i));
        applyHighlight();
    }
}
