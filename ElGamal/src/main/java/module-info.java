module com.example.elgamal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.elgamal to javafx.fxml;
    exports com.example.elgamal;
}