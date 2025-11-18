package com.example.projetopoo.jogo;

import com.example.projetopoo.ControladorCenas;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ResultadosController {

    @FXML
    private Button botaoMenu; // O botão que criaremos no FXML

    @FXML
    public void initialize() {
        botaoMenu.setOnAction(actionEvent -> {
            Stage stage = (Stage) botaoMenu.getScene().getWindow();
            try {
                ControladorCenas.carregarCena("Menu.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
