module com.example.projetopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires com.fazecast.jSerialComm;
    requires javafx.graphics;
    requires javafx.media;
    requires com.google.gson;

    exports com.example.projetopoo;
    opens com.example.projetopoo to javafx.fxml, com.google.gson;

    // Core
    exports com.example.projetopoo.jogo.core;
    opens com.example.projetopoo.jogo.core to com.google.gson, javafx.fxml;

    // Chart
    exports com.example.projetopoo.jogo.chart;
    opens com.example.projetopoo.jogo.chart to com.google.gson, javafx.fxml;

    // Logica
    exports com.example.projetopoo.jogo.logica;
    opens com.example.projetopoo.jogo.logica to com.google.gson, javafx.fxml;

    // Notas
    exports com.example.projetopoo.jogo.notas;
    opens com.example.projetopoo.jogo.notas to com.google.gson, javafx.fxml;

    // Renderer
    exports com.example.projetopoo.jogo.render;
    opens com.example.projetopoo.jogo.render to com.google.gson, javafx.fxml;
}

