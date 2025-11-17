module com.example.projetopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;
    requires com.google.gson;


    opens com.example.projetopoo to javafx.fxml;
    opens com.example.projetopoo.jogo to javafx.fxml, com.google.gson;
    exports com.example.projetopoo;
    exports com.example.projetopoo.jogo;

}