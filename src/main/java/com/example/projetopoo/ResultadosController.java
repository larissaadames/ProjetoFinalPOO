// @source_reference ResultadosController.java (novo)
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

    private static final int MAX_LIMITE_CARACTERES = 15;
    private static final int MIN_LIMITE_CARACTERES = 3;
    private static final String ESTILO_ERRO = "-fx-border-color: red; -fx-border-width: 3; -fx-background-color: #333333;";
    private static final String ESTILO_NORMAL = "-fx-border-color: transparent;";

    @FXML
    public void initialize() {
        botaoMenu.setOnAction(actionEvent -> salvarEVoltar());
        limitarCaracteres(inputNome, MAX_LIMITE_CARACTERES);
    }

    private void limitarCaracteres(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                // Trunca o texto se ele exceder o limite
                tf.setText(oldValue);
            }
        });
    }

    private void salvarEVoltar() {
        String nome = inputNome.getText().trim();

        // 1. VALIDAÇÃO MÍNIMA
        if (nome.length() < MIN_LIMITE_CARACTERES) {
            // Aplica estilo de erro e interrompe a função
            inputNome.setStyle("FFFFFF");
            // Poderia mostrar uma mensagem "Nome muito curto" aqui se quisesse.
            return;
        }

        // Se a validação passou, remove qualquer estilo de erro antigo
        inputNome.setStyle("FFFFFF");

        // apenas por seguranca
        if (nome.isEmpty()) {
            nome = "Anônimo";
        }

        // Cria o objeto Placar
        // String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        // Placar novoRecorde = new Placar(nome, pontuacaoFinal, dataHoje);

        // Salva no arquivo JSON
        // GerenciadorPlacar.adicionarPlacar(novoRecorde); // Descomente quando a classe GerenciadorPlacar estiver pronta

        // irParaMenu();
    }

    public void setDadosFinais(JogoEstado estado) {
        textScore.setText("Pontuação: " + estado.scoreProperty().get());
        textMaxCombo.setText("Maior Combo: " + estado.getMaxCombo());
        textPerfeito.setText("PERFEITO: " + estado.getAcertosPerfeito());
        textOtimo.setText("ÓTIMO: " + estado.getAcertosOtimo());
        textRuim.setText("RUIM: " + estado.getAcertosRuim());
        textErro.setText("ERROU: " + estado.getAcertosErro());
    }
}