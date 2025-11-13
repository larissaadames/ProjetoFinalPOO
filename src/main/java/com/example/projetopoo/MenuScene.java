package com.example.projetopoo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuScene extends Scene {

    public MenuScene(Stage stage) {
        super(new StackPane(), 900, 600);

        StackPane root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #000000, #202020);");

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        javafx.scene.control.Label title = new javafx.scene.control.Label("🎸 Arcade Rock 🎸");
        title.setFont(Font.font("Consolas", 36));
        title.setTextFill(Color.web("#FFD54A"));

        Button playBtn = makeButton("Selecionar Música");
        Button exitBtn = makeButton("Sair");

        playBtn.setOnAction(e -> {
            Scene songScene = new SongSelectScene(stage);
            stage.setScene(songScene);
        });

        exitBtn.setOnAction(e -> stage.close());

        box.getChildren().addAll(title, playBtn, exitBtn);
        root.getChildren().add(box);
    }

    private Button makeButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("Consolas", 20));
        b.setTextFill(Color.BLACK);
        b.setStyle("-fx-background-color: #FFD54A; -fx-padding: 10 24; -fx-background-radius: 10;");
        b.setOnMouseEntered(ev -> b.setStyle("-fx-background-color: #FFE082; -fx-padding: 10 24; -fx-background-radius: 10;"));
        b.setOnMouseExited(ev -> b.setStyle("-fx-background-color: #FFD54A; -fx-padding: 10 24; -fx-background-radius: 10;"));
        return b;
    }
}
