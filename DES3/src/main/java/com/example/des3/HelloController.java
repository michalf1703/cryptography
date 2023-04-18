package com.example.des3;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloController extends Component {
    private TripleDES tripledes = new TripleDES();
    private FileChooser fileChooser = new FileChooser();
    private byte tekst[];
    private byte szyfr[];
    private byte deszyfr[];
    private File plikKlucza, plikOdczytuTekstu, plikzapisuTekstu, plikOdczytuszyfr, plikzapisuSzyfr;
    private FileOperations pliczek;
    private BitOperations bicik;
    DES des = new DES();



    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;

    @FXML
    private TextField wczytaj_tekst;

    @FXML
    private TextField szyfruj_plik;


    @FXML
    void btnGenerujKlucz(ActionEvent event) {

        textField1.setText(des.generujKlucze());
        textField2.setText(des.generujKlucze());
        textField3.setText(des.generujKlucze());

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
    public void btnZapiszPlikTekst(ActionEvent event) {
        FileDialog dialog = new FileDialog((Frame)null, "Wybierz plik", FileDialog.SAVE);
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        if (fileName != null) {
            String filePath = dialog.getDirectory() + fileName;
            try {
                pliczek.zapiszDoPliku(wczytaj_tekst.getText().getBytes(), filePath);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Problem z zapisem do pliku" + filePath, "Problem z zapisem do pliku", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @FXML
    void btnPlikBinarny(ActionEvent event) throws IOException {
        File file = fileChooser.showOpenDialog(null);
        String sciezka = file.getPath();
        byte[] content = Files.readAllBytes(Paths.get(sciezka));
        try {
            tripledes.s_key1 = textField1.getText();
            tripledes.s_key2 = textField2.getText();
            tripledes.s_key3 = textField3.getText();
            szyfr = tripledes.encode3DES(content);
            szyfruj_plik.setText(bicik.bytesToHex(szyfr));
        } catch (DES.DESKeyException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE);
        }
        File file2 = fileChooser.showOpenDialog(null);
        OutputStream outputStream = new FileOutputStream(file2.getPath());
        outputStream.write(szyfr, 0,szyfr.length);
        outputStream.close();


    }
    @FXML
    void btnOdszyfrujPlikBinarny(ActionEvent event) throws IOException {

        try{
            tripledes.s_key1=textField1.getText();
            tripledes.s_key2=textField2.getText();
            tripledes.s_key3=textField3.getText();
            deszyfr =tripledes.decode3DES(bicik.hexToBytes(szyfruj_plik.getText()));
          //  wczytaj_tekst.setText(new String(deszyfr));
        } catch(DES.DESKeyException e){JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }

        File file1 = fileChooser.showOpenDialog(null);
        OutputStream outputStream2 = new FileOutputStream(file1.getPath());
        outputStream2.write(deszyfr, 0,deszyfr.length);
        outputStream2.close();
    }
    @FXML
    void btnSzyfrowanie(ActionEvent event) {
        try {
            tripledes.s_key1 = textField1.getText();
            tripledes.s_key2 = textField2.getText();
            tripledes.s_key3 = textField3.getText();
            szyfr = tripledes.encode3DES(wczytaj_tekst.getText().getBytes());
            szyfruj_plik.setText(bicik.bytesToHex(szyfr));
        } catch (DES.DESKeyException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void btnWczytajSzyfrPlik(ActionEvent event) {
        FileDialog fd = new FileDialog(new JFrame(), "Wybierz plik", FileDialog.LOAD);
        fd.setVisible(true);
        String fileName = fd.getFile();
        if (fileName != null) {
            String pom = fd.getDirectory() + fileName;
            plikOdczytuszyfr = new File(pom);
            try {
                szyfr = pliczek.wczytajZpliku(pom);
                szyfruj_plik.setText(new String(szyfr));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Problem z otworzeniem pliku" + pom, "Problem z otworzeniem pliku", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @FXML
    void btnZapiszSzyfr(ActionEvent event) {
        FileDialog fd = new FileDialog((Frame)null, "Zapisz plik", FileDialog.SAVE);
        fd.setVisible(true);
        String fileName = fd.getFile();
        String directory = fd.getDirectory();
        if (fileName != null && directory != null) {
            String path = directory + fileName;
          //  File file = new File(path);
            try {
                pliczek.zapiszDoPliku(szyfruj_plik.getText().getBytes(), path);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Problem z zapisem do pliku " + path, "Problem z zapisem do pliku", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    @FXML
    void btnDeszyfruj(ActionEvent event) {
        try{
            tripledes.s_key1=textField1.getText();
            tripledes.s_key2=textField2.getText();
            tripledes.s_key3=textField3.getText();
            deszyfr =tripledes.decode3DES(bicik.hexToBytes(szyfruj_plik.getText()));
            wczytaj_tekst.setText(new String(deszyfr));
        } catch(DES.DESKeyException e){JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
    }
}
