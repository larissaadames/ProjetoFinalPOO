module com.example.projetopoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.projetopoo to javafx.fxml;
    exports com.example.projetopoo;
}