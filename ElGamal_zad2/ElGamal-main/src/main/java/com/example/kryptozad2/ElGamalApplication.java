package com.example.kryptozad2;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ElGamalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ElGamalController mainController = new ElGamalController();
        mainController.showStage();
    }

    public static void main(String[] args) {
        launch();
    }
}