package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JogoRenderer {
    private Stage stage;
    private List<NotaSprite> sprites = new ArrayList<>();

    public JogoRenderer() {
        root.setPrefSize(800, 800);

        // linha onde a nota deve ser acertada
        Rectangle hitLine = new Rectangle(0, HIT_LINE, 800, 20);
        hitLine.setFill(Color.GOLDENROD);
        root.getChildren().add(hitLine);
    }

    public void iniciarCena(Stage stage) {
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }

    private Pane root = new Pane();

    private static final double NOTE_WIDTH = 80;
    private static final double NOTE_HEIGHT = 40;

    private static final double HIT_LINE = 600;



    public Pane getRoot() {
        return root;
    }

    public void atualizar(JogoLogica logica, double tempoMusicaMs) {
        for (Nota n : logica.getNotasAtivas()) {
            boolean existe = sprites.stream().anyMatch(s -> s.nota == n);

            if (!existe) {
                Rectangle r = new Rectangle(n.getX(), n.getY(), NOTE_WIDTH, NOTE_HEIGHT);
                r.setFill(Color.CYAN);

                sprites.add(new NotaSprite(n, r));
                root.getChildren().add(r);
            }
        }
    }
}