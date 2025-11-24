package com.example.projetopoo;

import com.example.projetopoo.controllers.ResultadosController;
import com.example.projetopoo.controllers.SeletorMusicaController;
import com.example.projetopoo.jogo.core.JogoEngine;
import com.example.projetopoo.jogo.core.JogoEstado;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import com.example.projetopoo.exceptions.SceneLoadException;

import java.io.IOException;
import java.util.Objects;

public class ControladorFluxo {

    private static Stage stageAtual;
    private static Scene scenePrincipal; // Mantemos uma referência única à cena

    private static final String[] ALL_SONG_IDS = { "goat", "brightside", "bmtl", "numb" };

    public static void iniciar(Stage stage) {
        stageAtual = stage;

        stageAtual.setTitle("PowerJorge - Arcade Rock");

        // Configura FullScreen e impede sair com ESC
        stageAtual.setFullScreenExitHint("");
        stageAtual.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // Cria a cena inicial vazia (será preenchida pelo irParaMenu)
        // Usamos um Group ou Pane vazio apenas para inicializar
        scenePrincipal = new Scene(new javafx.scene.layout.Pane(), 1920, 1080);
        stageAtual.setScene(scenePrincipal);

        stageAtual.setOnCloseRequest(event -> {
            HighScoreManager.getInstance().persistAll();
        });

        ArduinoConexao.getInstance().iniciar();

        irParaMenu(); // Isso vai preencher o conteúdo da scenePrincipal

        // Ativa o fullscreen e mostra
        stageAtual.setFullScreen(true);
        stageAtual.show();
    }

    // Método Mágico: Troca o conteúdo sem trocar a janela
    private static void mudarConteudo(String fxml, String cssName) {
        try {
            FXMLLoader loader = new FXMLLoader(ControladorFluxo.class.getResource(fxml));
            if (loader.getLocation() == null) throw new SceneLoadException("FXML não encontrado: " + fxml);

            Parent novoRoot = loader.load();

            // Troca o nó raiz da cena existente
            scenePrincipal.setRoot(novoRoot);

            // Atualiza o CSS
            scenePrincipal.getStylesheets().clear();
            if (cssName != null) {
                var urlCSS = ControladorFluxo.class.getResource(cssName);
                if (urlCSS != null) {
                    scenePrincipal.getStylesheets().add(urlCSS.toExternalForm());
                }
            }

            // Garante o foco para inputs
            Platform.runLater(novoRoot::requestFocus);

        } catch (IOException e) {
            throw new SceneLoadException("Erro ao carregar tela: " + fxml, e);
        }
    }

    public static void irParaMenu() {
        mudarConteudo("Menu.fxml", "menu.css");
    }

    public static void irParaCreditos() {
        mudarConteudo("Creditos.fxml", "menu.css");
    }

    public static void irParaSelecaoMusicas() {
        try {
            FXMLLoader loader = new FXMLLoader(ControladorFluxo.class.getResource("cenaSeletorMusica.fxml"));
            Parent root = loader.load();

            SeletorMusicaController controller = loader.getController();
            controller.setOnBack(ControladorFluxo::irParaMenu);
            controller.setOnConfirm(i -> irParaJogo(ALL_SONG_IDS[i]));

            // Troca o conteúdo manualmente pois este método tem lógica extra
            scenePrincipal.setRoot(root);

            scenePrincipal.getStylesheets().clear();
            var css = ControladorFluxo.class.getResource("seletor.css");
            if (css != null) scenePrincipal.getStylesheets().add(css.toExternalForm());

            Platform.runLater(root::requestFocus);

        } catch (IOException e) {
            throw new SceneLoadException("Erro no Seletor", e);
        }
    }

    public static void irParaJogo(String songId) {
        try {
            JogoEngine engine = new JogoEngine(songId, stageAtual);
            if (Objects.equals(songId, "bmtl")) engine.iniciar(25);
            else engine.iniciar(0);
        } catch (Exception e) {
            throw new SceneLoadException("Erro ao carregar Jogo", e);
        }
    }

    public static void irParaTelaFinal(String songId, JogoEstado estadoFinal) {
        try {
            FXMLLoader loader = new FXMLLoader(ControladorFluxo.class.getResource("Resultados.fxml"));
            Parent root = loader.load();

            ResultadosController controller = loader.getController();
            controller.setDadosFinais(songId, estadoFinal);

            scenePrincipal.setRoot(root);
            // scenePrincipal.getStylesheets().clear(); // Se tiver CSS específico, adicione aqui

            Platform.runLater(root::requestFocus);

        } catch (IOException e) {
            throw new SceneLoadException("Erro na tela final", e);
        }
    }

    public static Stage getStageAtual() { return stageAtual; }

    // Método útil se o JogoEngine precisar acessar a cena
    public static Scene getScenePrincipal() { return scenePrincipal; }
}