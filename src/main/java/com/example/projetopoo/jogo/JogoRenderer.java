package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JogoRenderer {
    private final List<HitDot> hitDots = new ArrayList<>();
    private Stage stage;
    private final Map<Nota, INotaSprite> spritesMap = new HashMap<>();
    private final Group root = new Group();

    public JogoRenderer() {
        root.resize(1920, 1080);

        Rectangle hitLine = new Rectangle(Layout.INICIO_X - 20, Layout.HIT_LINE, Layout.AREA_JOGO_SIZE, 20);
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

        for (Nota nota : logica.getNotasAtivas()) {

            INotaSprite sprite = spritesMap.get(nota);

            if (sprite == null) {
                sprite = nota.criarSprite();

                spritesMap.put(nota, sprite);
                root.getChildren().add(sprite.getNode());
            }

            sprite.atualizar(tempoMusicaMs);
        }

        spritesMap.entrySet().removeIf(entry -> {
            Nota nota = entry.getKey();
            INotaSprite sprite = entry.getValue();

            if(!nota.isAtiva()) {
                root.getChildren().remove(sprite.getNode());
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

    public List<HitDot> getHitDots() {
        return hitDots;
    }
}