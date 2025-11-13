package com.example.projetopoo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SongSelectScene extends Scene {

    public SongSelectScene(Stage stage) {
        super(new StackPane(), 900, 600);

        StackPane root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #111111, #222222);");

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("🎶 Seleção de Músicas 🎶");
        title.setFont(Font.font("Consolas", 30));
        title.setTextFill(Color.web("#90CAF9"));

        Button backBtn = makeButton("← Voltar ao Menu");

        // Botão de teste: volta ao menu
        backBtn.setOnAction(e -> {
            Scene menu = new MenuScene(stage);
            stage.setScene(menu);
        });

        // Só pra enfeitar: botões de “músicas”
        Button music1 = makeButton("🔥 Highway Star");
        Button music2 = makeButton("⚡ Thunderstruck");
        Button music3 = makeButton("🎸 Back in Black");

        box.getChildren().addAll(title, music1, music2, music3, backBtn);
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
