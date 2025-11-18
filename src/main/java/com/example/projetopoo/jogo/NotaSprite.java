package com.example.projetopoo.jogo;

import javafx.scene.Node;

public abstract class NotaSprite {
    Nota nota;

    public abstract void atualizarSprite(double deltatime, double tempoMusicaMs);

    public abstract Node getNode();

    public Nota getNota() {
        return nota;
    }

//    protected NotaSprite(Nota nota) {
//        this.nota = nota;
//    }
//
//    public Nota getNota() { return nota; }
//
//    public abstract void criarSprite(Group root);
//
//    //    public void criarSprite(Group root) {
//    //        circle = new Circle(Layout.RAIO_CIRCLE);
//    //        circle.setFill(Color.CYAN);
//    //        circle.setLayoutX(nota.getLaneX());
//    //        circle.setLayoutY(nota.getY());
//    //        root.getChildren().add(circle);
//    //    }
//
//    public abstract void atualizarSprite(double dt, double tempo);
//
//    public abstract void removerSprite(Group root);
}



