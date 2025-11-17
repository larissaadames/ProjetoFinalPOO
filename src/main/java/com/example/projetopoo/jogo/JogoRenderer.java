package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class JogoRenderer {
    private final List<HitDot> hitDots = new ArrayList<>();
    private Stage stage;
    private final List<NotaTapSprite> sprites = new ArrayList<>();
    private final Group root = new Group();


    public JogoRenderer() {
        root.resize(1920, 1080);

        Rectangle hitLine = new Rectangle(Layout.INICIO_X - 20, Nota.HIT_LINE, Layout.AREA_JOGO_SIZE, 20);
        hitLine.setFill(Color.GOLDENROD);
        root.getChildren().add(hitLine);
        criarHitDots();
    }

    public void iniciarCena(Stage stage) {
        Scene cena = new Scene(root, 1920, 1080);
        cena.setFill(Color.web("010101"));
        stage.setScene(cena);
        stage.show();
    }

    public Group getRoot() {
        return root;
    }

    public void atualizar(JogoLogica logica, double tempoMusicaMs, double deltaTime) {
        List<Nota> notasAtivas = logica.getNotasAtivas();

            for (Nota nota : logica.getNotasAtivas()) {

                NotaTapSprite sprite = encontrarSprite(nota);

                if (sprite == null) {
                    Circle c = new Circle(Layout.RAIO_CIRCLE) ;
                    c.setFill(Color.CYAN);

                    sprite = new NotaTapSprite(nota, c);
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

    private void criarHitDots() {
        Color[] coresLanes = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE };
        double posY = 950;
        double spacing = 150;

        for (int i = 0; i < 5; i++) {
            double x = Layout.INICIO_X + i * spacing + Layout.RAIO_CIRCLE;
            HitDot dot = new HitDot(i + 1, x, posY, coresLanes[i]);
            hitDots.add(dot);
            root.getChildren().add(dot.getCircle());
        }
    }

        private NotaTapSprite encontrarSprite(Nota nota) {
            for (NotaTapSprite sprite : sprites) {
                if(sprite.getNota() == nota) {
                    return sprite;
                }
            }
        return null;
    }

    public List<HitDot> getHitDots() {
        return hitDots;
    }
}