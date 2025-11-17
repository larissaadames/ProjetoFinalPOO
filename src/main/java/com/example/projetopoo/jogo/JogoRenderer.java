package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class JogoRenderer {
    private Stage stage;


    public void iniciarCena(Stage stage) {
        Group root = new Group();
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }

    private Pane root = new Pane();
    private final Map<Nota, Rectangle> mapaSprites = new HashMap<>();

    private static final double NOTE_WIDTH = 80;
    private static final double NOTE_HEIGHT = 40;

    private static final double HIT_LINE = 600;

    public JogoRenderer() {
        root.setPrefSize(800, 800);

        // linha onde a nota deve ser acertada
        Rectangle hitLine = new Rectangle(0, HIT_LINE, 800, 5);
        hitLine.setFill(Color.RED);
        root.getChildren().add(hitLine);
    }

    public Pane getRoot() {
        return root;
    }

    public void atualizarRender(JogoLogica logica, double tempoMusicaMs) {

        for (Nota nota : logica.getNotasAtivas()) {
            if (!mapaSprites.containsKey(nota)) {
                Rectangle r = new Rectangle(
                        nota.getLane() * 100 + 100,
                        nota.getY(),
                        NOTE_WIDTH,
                        NOTE_HEIGHT
                );
                r.setFill(Color.CYAN);
                mapaSprites.put(nota, r);
                root.getChildren().add(r);
            }
        }

        mapaSprites.forEach((nota, rect) -> {
            rect.setY(nota.getY());
        });

        logica.getNotasAtivas().removeIf(nota -> {
            if (nota.deveDespawnar(tempoMusicaMs)){
                Rectangle r = mapaSprites.remove(nota);
                root.getChildren().remove(r);
                return true;
            }
            return false;
        });
    }
}