module com.example.des3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.des3 to javafx.fxml;
    exports com.example.des3;
}