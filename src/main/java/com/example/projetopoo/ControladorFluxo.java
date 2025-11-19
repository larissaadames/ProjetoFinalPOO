package com.example.projetopoo;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

// Apenas adicionamos as EXCEPTIONS aqui.
import com.example.projetopoo.exceptions.SceneLoadException;

public class ControladorFluxo {

    private static Stage stageAtual;

    // Lista de IDs de música (baseado no SeletorMusicaController: allstar, numb, bmtl)
    private static final String[] ALL_SONG_IDS = { "allstar", "numb", "bmtl" };

    public static void iniciar(Stage stage) {
        stageAtual = stage;

        stageAtual.setTitle("PowerJorge - Arcade Rock");
        stageAtual.setWidth(1920);
        stageAtual.setHeight(1080);
        stageAtual.setResizable(false);
        stageAtual.centerOnScreen();

        // Adiciona o Gancho de Desligamento (Shutdown Hook) para a persistência.
        stageAtual.setOnCloseRequest(event -> {
            HighScoreManager.getInstance().persistAll();
        });

        irParaMenu();
        stageAtual.show();
    }

    private static Scene carregarComCSS(String fxml, String cssName) throws IOException {
        FXMLLoader loader = new FXMLLoader(ControladorFluxo.class.getResource(fxml));

        if (loader.getLocation() == null)
            throw new SceneLoadException("FXML não encontrado: " + fxml);

        Parent root = loader.load();

        Scene cena = new Scene(root, stageAtual.getWidth(), stageAtual.getHeight());

        if (cssName != null) {
            var urlCSS = ControladorFluxo.class.getResource(cssName);

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
                    ControladorFluxo.class.getResource("cenaSeletorMusica.fxml")
            );

            if (loader.getLocation() == null)
                throw new SceneLoadException("cenaSeletorMusica.fxml não encontrado.");

            Parent root = loader.load();
            Scene cena = new Scene(root, 1920, 1080);

            var css = ControladorFluxo.class.getResource("seletor.css");
            if (css == null)
                throw new SceneLoadException("seletor.css não encontrado.");

            cena.getStylesheets().add(css.toExternalForm());

            SeletorMusicaController controller = loader.getController();

            controller.setOnBack(ControladorFluxo::irParaMenu);

            // 🌟 MUDANÇA: Agora passa o songId (String) em vez do índice (int) para o jogo.
            controller.setOnConfirm(i -> {
                String songId = ALL_SONG_IDS[i];
                irParaJogo(songId); // Chama a nova versão de irParaJogo
            });

            stageAtual.setScene(cena);
            Platform.runLater(() -> root.requestFocus());

        } catch (IOException e) {
            throw new SceneLoadException("Erro ao carregar cenaSeletorMusica.fxml", e);
        }
    }

    // 🌟 NOVO: Recebe o songId da música selecionada para iniciar a partida.
    public static void irParaJogo(String songId) {
        try {
            // O controller da TelaJogo.fxml deve ser adaptado para receber este songId.
            Scene cena = carregarComCSS("TelaJogo.fxml", "jogo.css");
            stageAtual.setScene(cena);

            // Ao final do jogo, o TelaJogoController chamará:
            // ControladorCenas.irParaTelaFinal(songId, scoreFinal);

        } catch (Exception e) {
            throw new SceneLoadException("Erro ao carregar TelaJogo.fxml", e);
        }
    }

    // 🌟 NOVO: Recebe o songId e o score para a tela de finalização/salvamento.
    public static void irParaTelaFinal(String songId, int score) {
        try {
            // O controller da TelaFinal.fxml será responsável por obter o nome do jogador
            // e chamar HighScoreManager.getInstance().addScore(songId, playerName, score);
            Scene cena = carregarComCSS("TelaFinal.fxml", null);
            stageAtual.setScene(cena);
        } catch (Exception e) {
            throw new SceneLoadException("Erro ao carregar TelaFinal.fxml", e);
        }
    }
}