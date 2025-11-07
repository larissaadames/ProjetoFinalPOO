module com.example.projetopoo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projetopoo to javafx.fxml;
    exports com.example.projetopoo;
}