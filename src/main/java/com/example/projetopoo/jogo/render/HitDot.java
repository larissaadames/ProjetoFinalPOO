package com.example.projetopoo.jogo.render;

import com.example.projetopoo.jogo.core.Layout;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class HitDot {
    private final int lane;
    private final Circle circle;

    public HitDot(int lane, double x, double y, Color cor) {
        this.lane = lane;
        this.circle = new Circle(Layout.RAIO_CIRCLE);
        this.circle.setFill(cor);
        this.circle.setLayoutX(x);
        this.circle.setLayoutY(y);
        this.circle.setOpacity(0.3);
    }

    public Circle getCircle() {
        return circle;
    }

    public int getLane() {
        return lane;
    }

    public void piscar(boolean acerto) {
        double alvo = acerto ? 1.0 : 0.6;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(circle.opacityProperty(), circle.getOpacity())),
                new KeyFrame(Duration.millis(100), new KeyValue(circle.opacityProperty(), alvo)),
                new KeyFrame(Duration.millis(300), new KeyValue(circle.opacityProperty(), 0.3))
        );
        timeline.play();
    }

    public void manter(boolean pressionado) {
        // Para HOLD: se pressionado, brilho fica mais alto; senão volta para base
        circle.setOpacity(pressionado ? 0.8 : 0.3);
    }
}

