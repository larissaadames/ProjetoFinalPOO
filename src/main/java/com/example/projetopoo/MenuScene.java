package com.example.projetopoo;

import com.example.projetopoo.aplicacao.Main;
import com.example.projetopoo.controllers.SeletorMusicaController;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class MenuScene extends Scene {

    public MenuScene(Stage stage) {
        super(new StackPane(), 900, 600);

        StackPane root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #000000, #202020);");

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("🎸 Arcade Rock 🎸");
        title.setFont(Font.font("Consolas", 36));
        title.setTextFill(Color.web("#FFD54A"));

        Button playBtn = makeButton("Selecionar Música");
        Button exitBtn = makeButton("Sair");

        // 👉 AQUI trocamos para abrir o seletor NOVO (FXML)
        playBtn.setOnAction(e -> openSongSelector(stage));

        exitBtn.setOnAction(e -> stage.close());

        box.getChildren().addAll(title, playBtn, exitBtn);
        root.getChildren().add(box);
    }

    private Button makeButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("Consolas", 20));
        b.setTextFill(Color.BLACK);
        b.setStyle("-fx-background-color: #FFD54A; -fx-padding: 10 24; -fx-background-radius: 10;");
        b.setOnMouseEntered(ev ->
                b.setStyle("-fx-background-color: #FFE082; -fx-padding: 10 24; -fx-background-radius: 10;"));
        b.setOnMouseExited(ev ->
                b.setStyle("-fx-background-color: #FFD54A; -fx-padding: 10 24; -fx-background-radius: 10;"));
        return b;
    }

    // 🔻 Carrega a tela de seleção FXML + CSS e configura callbacks
    private void openSongSelector(Stage stage) {
        try {
            FXMLLoader fx = new FXMLLoader(Main.class.getResource("cenaSeletorMusica.fxml"));
            Parent root = fx.load();
            SeletorMusicaController ctrl = fx.getController();

            // cena da seleção
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(
                    Main.class.getResource("seletor.css").toExternalForm()
            );

            // ESC → volta para o menu
            ctrl.setOnBack(() -> stage.setScene(new MenuScene(stage)));

            // ENTER → por enquanto só imprime; depois você coloca a cena do jogo
            ctrl.setOnConfirm(idx -> {
                System.out.println("Selecionou card #" + idx);
            });

            stage.setScene(scene);

        } catch (IOException ex) {
            ex.printStackTrace(); // em trabalho real você trataria melhor esse erro
        }
    }
}
