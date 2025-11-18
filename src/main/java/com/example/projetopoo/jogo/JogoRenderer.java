package com.example.projetopoo.jogo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class JogoRenderer {

    private final List<HitDot> hitDots = new ArrayList<>();
    private final List<NotaSprite> sprites = new ArrayList<>();
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

    // 🔥 Atualiza sprites de acordo com as notas da lógica
    public void atualizar(JogoLogica logica, double tempoMusicaMs, double deltaTime) {

        for (Nota nota : logica.getNotasAtivas()) {

            NotaSprite sprite = encontrarSprite(nota);

            if (sprite == null) {
                //  NOTA EM SI DECIDE QUAL SPRITE CRIAR!
                sprite = nota.criarSprite();
                sprites.add(sprite);

                root.getChildren().add(sprite.getNode());
            }

            //  Cada sprite atualiza sua própria posição
            sprite.atualizarSprite(deltaTime, tempoMusicaMs);
        }

        //  remove sprites de notas que morreram
        sprites.removeIf(sprite -> {
            if (!sprite.getNota().isAtiva()) {
                root.getChildren().remove(sprite.getNode());
                return true;
            }
            return false;
        });
    }

    private void criarHitDots() {
        Color[] coresLanes = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE };
        double posY = 950;
        double spacing = 125;

        for (int i = 0; i < 5; i++) {
            double x = Layout.INICIO_X + i * spacing + Layout.RAIO_CIRCLE;
            HitDot dot = new HitDot(i + 1, x, posY, coresLanes[i]);
            hitDots.add(dot);
            root.getChildren().add(dot.getCircle());
        }
    }

    private NotaSprite encontrarSprite(Nota nota) {
        for (NotaSprite sprite : sprites) {
            if (sprite.getNota() == nota) return sprite;
        }
        return null;
    }

    public List<HitDot> getHitDots() {
        return hitDots;
    }
}
