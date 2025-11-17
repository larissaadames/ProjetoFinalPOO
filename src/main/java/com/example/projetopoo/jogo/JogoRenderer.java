package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class JogoRenderer {
    private Stage stage;
    private List<NotaSprite> sprites = new ArrayList<>();

    public JogoRenderer() {
        root.setPrefSize(1920, 1080);

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

    private static
    final double HIT_LINE = 600;



    public Pane getRoot() {
        return root;
    }

    public void atualizar(JogoLogica logica, double tempoMusicaMs) {
        List<Nota> notasAtivas = logica.getNotasAtivas();

            for (Nota nota : logica.getNotasAtivas()) {

                NotaSprite sprite = encontrarSprite(nota);

                if (sprite == null) {
                    Circle c = new Circle(40) ;
                    c.setFill(Color.CYAN);

                    sprite = new NotaSprite(nota, c);
                    sprites.add(sprite);
                    root.getChildren().add(c);
                }

                sprite.getCircle().setLayoutX(nota.getLaneX());
                sprite.getCircle().setLayoutY(nota.getY());
            }

            sprites.removeIf(sprite -> {
                if (!sprite.getNota().isAtiva()) {
                    root.getChildren().remove(sprite.getCircle());
                    return true;
                }
                return false;
            });
        }

        private NotaSprite encontrarSprite(Nota nota) {
            for (NotaSprite sprite : sprites) {
                if(sprite.getNota() == nota) {
                    return sprite;
                }
            }
        return null;
    }

}