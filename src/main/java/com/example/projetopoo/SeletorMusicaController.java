// com/example/projetopoo/SeletorMusicaController.java
package com.example.projetopoo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SeletorMusicaController {
    @FXML private Button botaoJogar;
    @FXML private Button botaoPlacar;

    private Runnable onJogar;
    private Runnable onPlacar;

    public void setOnJogar(Runnable r){ this.onJogar = r; }
    public void setOnPlacar(Runnable r){ this.onPlacar = r; }

    @FXML
    private void initialize(){
        botaoJogar.setOnAction(e -> { if (onJogar != null) onJogar.run(); });
        botaoPlacar.setOnAction(e -> { if (onPlacar != null) onPlacar.run(); });
    }
}

