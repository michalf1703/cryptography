package com.example.elgamal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;

public class HelloController {

    FileOperations pliczek = new FileOperations();
    ElGamal elGamal = new ElGamal();
    private byte tekst[];
    private File plikOdczytuTekstu;
    @FXML
    private Button bntSzyfrowanie;

    @FXML
    private Button btnWczytajSzyfr;

    @FXML
    private Button btn_zapisz_tekst;

    @FXML
    private Button button_odszyfruj_plik_binarny;

    @FXML
    private Button button_wczytaj_plik_binarny;

    @FXML
    private Button button_wczytaj_tekst;

    @FXML
    private Button deszyfruj;

    @FXML
    private TextField szyfruj_plik;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private TextField textField4;

    @FXML
    private TextField wczytaj_tekst;

    @FXML
    private Label welcomeText;

    @FXML
    private Button zapisz_szyfr;

    @FXML
    void btnDeszyfruj(ActionEvent event) {

    }

    @FXML
    void btnGenerujKlucz(ActionEvent event) {
        elGamal.generateKey();
        textField1.setText(elGamal.g.toString(16));
        textField2.setText(elGamal.h.toString(16));
        textField3.setText(elGamal.a.toString(16));
        textField4.setText(elGamal.N.toString(16));

    }

    @FXML
    void btnOdszyfrujPlikBinarny(ActionEvent event) {

    }

    @FXML
    void btnPlikBinarny(ActionEvent event) {

    }

    @FXML
    void btnSzyfrowanie(ActionEvent event) {

    }

    @FXML
    void btnWczytajPlikTekst(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String pom = selectedFile.getAbsolutePath();
            plikOdczytuTekstu = new File(pom);
            try {
                tekst = pliczek.wczytajZpliku(pom);
                wczytaj_tekst.setText(new String(tekst));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Problem z otworzeniem pliku" + pom, "Problem z otworzeniem pliku", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    @FXML
    void btnWczytajSzyfrPlik(ActionEvent event) {

    }

    @FXML
    void btnZapiszPlikTekst(ActionEvent event) {

    }

    @FXML
    void btnZapiszSzyfr(ActionEvent event) {

    }

}
