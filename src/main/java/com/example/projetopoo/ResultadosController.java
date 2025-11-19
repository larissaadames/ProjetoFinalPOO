package com.example.projetopoo;

import com.example.projetopoo.jogo.core.JogoEstado;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ResultadosController {

    @FXML private Button botaoMenu;
    @FXML private Text textScore;
    @FXML private Text textMaxCombo;
    @FXML private Text textPerfeito;
    @FXML private Text textOtimo;
    @FXML private Text textRuim;
    @FXML private Text textErro;
    @FXML private TextField inputNome;

    // Variáveis para guardar os dados recebidos da partida
    private JogoEstado estadoFinal;
    private String songId;

    private static final int MAX_LIMITE_CARACTERES = 12;
    private static final int MIN_LIMITE_CARACTERES = 3;

    @FXML
    public void initialize() {
        // Configura o botão para chamar o método de salvar
        botaoMenu.setOnAction(actionEvent -> salvarEVoltar());

        // Limita o tamanho do texto
        limitarCaracteres(inputNome, MAX_LIMITE_CARACTERES);
    }

    // Chamado pelo ControladorFluxo ao abrir a tela
    public void setDadosFinais(String songId, JogoEstado estado) {
        // CORREÇÃO AQUI: Atribuir o parâmetro 'songId' ao atributo da classe 'this.songId'
        this.songId = songId;
        this.estadoFinal = estado;

        // Atualiza a interface com os valores reais
        if (estado != null) {
            textScore.setText("Pontuação: " + estado.scoreProperty().get());
            textMaxCombo.setText("Maior Combo: " + estado.getMaxCombo());
            textPerfeito.setText("PERFEITO: " + estado.getAcertosPerfeito());
            textOtimo.setText("ÓTIMO: " + estado.getAcertosOtimo());
            textRuim.setText("RUIM: " + estado.getAcertosRuim());
            textErro.setText("ERROU: " + estado.getAcertosErro());
        }
    }

    private void salvarEVoltar() {
        String nome = inputNome.getText().trim();

        // 1. Validação Simples
        if (nome.length() < MIN_LIMITE_CARACTERES) {
            inputNome.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            inputNome.setPromptText("Mínimo 3 letras!");
            return;
        }

        // 2. Salvar no HighScoreManager (CSV)
        if (estadoFinal != null && songId != null) {
            // Pega a pontuação final do estado
            int scoreFinal = estadoFinal.scoreProperty().get();

            // Chama o Singleton para salvar no arquivo highscores.csv
            // O HighScoreManager já tem a lógica de leitura/escrita pronta.
            HighScoreManager.getInstance().addScore(songId, nome, scoreFinal);

            System.out.println("Score salvo com sucesso: " + nome + " | " + scoreFinal + " | Música: " + songId);
        } else {
            System.err.println("Erro ao salvar: songId ou estadoFinal nulos.");
        }

        // 3. Voltar para o Menu
        ControladorFluxo.irParaMenu();
    }

    private void limitarCaracteres(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                tf.setText(oldValue);
            }
        });
    }
}