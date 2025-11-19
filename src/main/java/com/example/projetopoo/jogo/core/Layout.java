package com.example.projetopoo.jogo.core;

import javafx.scene.paint.Color;

public class Layout {
    public static final double INICIO_X = 660;
    public static final double AREA_JOGO_SIZE = 600;
    public static final double LANE_SIZE = (INICIO_X + AREA_JOGO_SIZE) / 5;
    public static final double FINAL_X = INICIO_X + AREA_JOGO_SIZE;
    public static final double RAIO_CIRCLE = 60;
    public static final double HIT_LINE = 900;

    public static Color getCorLane(int lane) {
        return switch (lane) {
            case 1 -> Color.RED;
            case 2 -> Color.ORANGE;
            case 3 -> Color.YELLOW;
            case 4 -> Color.GREEN;
            case 5 -> Color.BLUE;
            default -> Color.RED;
        };
    }
}
