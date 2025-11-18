package com.example.projetopoo;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

// Apenas adicionamos as EXCEPTIONS aqui.
import com.example.projetopoo.exceptions.SceneLoadException;
import com.example.projetopoo.exceptions.SongNotFoundException;

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

        if (loader.getLocation() == null)
            throw new SceneLoadException("FXML não encontrado: " + fxml);

        Parent root = loader.load();

        Scene cena = new Scene(root, stageAtual.getWidth(), stageAtual.getHeight());

        if (cssName != null) {
            var urlCSS = ControladorCenas.class.getResource(cssName);

            if (urlCSS == null)
                throw new SceneLoadException("CSS não encontrado: " + cssName);

            cena.getStylesheets().add(urlCSS.toExternalForm());
        }

        return cena;
    }

    public static void irParaMenu() {
        try {
            Scene cena = carregarComCSS("Menu.fxml", "menu.css");
            stageAtual.setScene(cena);

            Platform.runLater(() -> cena.getRoot().requestFocus());

        } catch (Exception e) {
            throw new SceneLoadException("Erro ao carregar Menu.fxml", e);
        }
    }

    public static void irParaSelecaoMusicas() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    ControladorCenas.class.getResource("cenaSeletorMusica.fxml")
            );

            if (loader.getLocation() == null)
                throw new SceneLoadException("cenaSeletorMusica.fxml não encontrado.");

            Parent root = loader.load();
            Scene cena = new Scene(root, 1920, 1080);

            var css = ControladorCenas.class.getResource("seletor.css");
            if (css == null)
                throw new SceneLoadException("seletor.css não encontrado.");

            cena.getStylesheets().add(css.toExternalForm());

            SeletorMusicaController controller = loader.getController();

            controller.setOnBack(ControladorCenas::irParaMenu);

            // ⚠️ MANTIDO EXATAMENTE COMO VOCÊ TINHA NO CÓDIGO ORIGINAL
            controller.setOnConfirm(i -> irParaJogo());

            stageAtual.setScene(cena);
            Platform.runLater(() -> root.requestFocus());

        } catch (IOException e) {
            throw new SceneLoadException("Erro ao carregar cenaSeletorMusica.fxml", e);
        }
    }

    public static void irParaJogo() {
        try {
            Scene cena = carregarComCSS("TelaJogo.fxml", "jogo.css");
            stageAtual.setScene(cena);
        } catch (Exception e) {
            throw new SceneLoadException("Erro ao carregar TelaJogo.fxml", e);
        }
    }

    public static void irParaTelaFinal() {
        try {
            Scene cena = carregarComCSS("TelaFinal.fxml", null);
            stageAtual.setScene(cena);
        } catch (Exception e) {
            throw new SceneLoadException("Erro ao carregar TelaFinal.fxml", e);
        }
    }
}
