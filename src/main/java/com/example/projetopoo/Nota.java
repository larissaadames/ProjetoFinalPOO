package com.example.projetopoo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Nota {
    private final float scrollSpeed;
    private final Circle circle;
    private double posX;
    private double posY;
    private String cor = "#00F000"; // hexadecimal

    Nota(double posX,double posY,String cor, float scrollSpeed) {
        circle = new Circle(posX, posY, 25, Color.web(cor));
        this.scrollSpeed = scrollSpeed;

    }


    public Circle getCircle() {
        return circle;
    }

    public void moverNota(double deltaTime) {
        this.circle.setCenterY(this.circle.getCenterY() + scrollSpeed * deltaTime);
    }

    public void foraDatela() {
        if(this.circle.getCenterY() >= 1000) this.circle.setCenterY(100);
    }

}


