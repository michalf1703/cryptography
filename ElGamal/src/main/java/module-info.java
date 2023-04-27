module com.example.elgamal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.elgamal to javafx.fxml;
    exports com.example.elgamal;
}