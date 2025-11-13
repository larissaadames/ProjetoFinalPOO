module com.example.projetopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens com.example.projetopoo to javafx.fxml;
    exports com.example.projetopoo;
}