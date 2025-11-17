package com.example.projetopoo;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ControladorCenas {

    private static Stage stageAtual;

    public static void iniciar(Stage stage) {
        stageAtual = stage;

        stageAtual.setTitle("PowerJorge - Arcade Rock");
        stageAtual.setWidth(1920);
        stageAtual.setHeight(1080);
        stageAtual.setResizable(false);
        stageAtual.centerOnScreen();

        irParaMenu();
        stageAtual.show();
    }

    private static Scene carregarComCSS(String fxml, String cssName) throws IOException {
        FXMLLoader loader = new FXMLLoader(ControladorCenas.class.getResource(fxml));
        Parent root = loader.load();

        Scene cena = new Scene(root, stageAtual.getWidth(), stageAtual.getHeight());

        if (cssName != null) {
            cena.getStylesheets().add(
                    Objects.requireNonNull(
                            ControladorCenas.class.getResource(cssName)
                    ).toExternalForm()
            );
        }

        return cena;
    }

    public static void irParaMenu() {
        try {
            Scene cena = carregarComCSS("Menu.fxml", "menu.css");
            stageAtual.setScene(cena);

            Platform.runLater(() -> cena.getRoot().requestFocus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void irParaSelecaoMusicas() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    ControladorCenas.class.getResource("cenaSeletorMusica.fxml")
            );
            Parent root = loader.load();

            Scene cena = new Scene(root, 1920, 1080);


            // CSS DO SELETOR — ESSENCIAL
            cena.getStylesheets().add(
                    Objects.requireNonNull(
                            ControladorCenas.class.getResource("seletor.css")
                    ).toExternalForm()
            );

            // pega o controller
            SeletorMusicaController controller = loader.getController();

            // bind ESC
            controller.setOnBack(ControladorCenas::irParaMenu);

            // bind ENTER
            controller.setOnConfirm(i -> irParaJogo());

            stageAtual.setScene(cena);

            // garante foco
            Platform.runLater(() -> root.requestFocus());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar cena do seletor.");
        }
    }

    public static void irParaJogo() {
        try {
            Scene cena = carregarComCSS("TelaJogo.fxml", "jogo.css");
            stageAtual.setScene(cena);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void irParaTelaFinal() {
        try {
            Scene cena = carregarComCSS("TelaFinal.fxml", null);
            stageAtual.setScene(cena);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}