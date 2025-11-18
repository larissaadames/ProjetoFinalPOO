// @source_reference ResultadosController.java (novo)
package com.example.projetopoo;

import com.example.projetopoo.jogo.JogoEstado;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class ResultadosController {

    @FXML private Button botaoMenu;
    @FXML private Text textScore;
    @FXML private Text textMaxCombo;
    @FXML private Text textPerfeito;
    @FXML private Text textOtimo;
    @FXML private Text textRuim;
    @FXML private Text textErro;

    @FXML
    public void initialize() {
        botaoMenu.setOnAction(actionEvent -> {
            Stage stage = (Stage) botaoMenu.getScene().getWindow();
            try {
                // Volta para a cena do Menu Principal
                ControladorCenas.carregarCena("Menu.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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