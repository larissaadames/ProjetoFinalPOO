module com.example.projetopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens com.example.projetopoo to javafx.fxml;
    exports com.example.projetopoo;
    exports com.example.projetopoo.jogo;
    opens com.example.projetopoo.jogo to javafx.fxml;
}