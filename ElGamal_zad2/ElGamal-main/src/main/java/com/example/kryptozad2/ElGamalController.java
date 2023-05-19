package com.example.kryptozad2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ElGamalController {

    @FXML
    private Button btnWczytajSzyfr;

    @FXML
    private Button btnWczytajSzyfr1;
    private File plikOdczytuTekstu,plikOdczytuszyfr, plikzapisuSzyfr;

    @FXML
    private Button button_wczytaj_plik_binarny;

    @FXML
    private Button button_wczytaj_tekst;

    @FXML
    private Button decryptButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button btn_odszyfruj_binarny;

    @FXML
    private Button btn_zaszyfruj_plik_binarny;

    @FXML
    private Button generateKeysButton;

    @FXML
    private Label infoLabel;

    @FXML
    private AnchorPane mainBackground;

    @FXML
    private VBox mainBorder;

    @FXML
    private TextField privateKey;

    @FXML
    private TextField publicKeyG;

    @FXML
    private TextField publicKeyH;

    @FXML
    private TextField publicKeyP;

    @FXML
    private Button btn_zapisz_tekst;

    @FXML
    private Button setFile;

    @FXML
    private TextArea textToDecrypt;

    @FXML
    private TextArea textToEncrypt;
    @FXML
    private Button btn_zapisz_szyfr_z_okienka;

    FileOperations pliczek = new FileOperations();
    byte[] content;
    private byte tekst[];
    ElGamalController mainController;

    Stage stage = new Stage();

    File file;

    BigInteger p;
    BigInteger h;
    BigInteger g;
    BigInteger pk;


    public void showStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElGamalApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 600);
        mainController = fxmlLoader.getController();
        mainController.defaultSettings();
        stage.setTitle("ElGamal | Michal Ferdzyn 242383 | Artur Grzybek 242399");
        stage.setScene(scene);
        stage.show();
    }

    public void defaultSettings() {
        ToggleGroup toggleGroup = new ToggleGroup();
        encryptButton.setOnAction(a -> encryptMessage());
        decryptButton.setOnAction(a -> decryptMessage());
        setFile.setOnAction(a -> {
            try {
                loadFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        generateKeysButton.setOnAction(a -> generateKeys());
        btn_zapisz_tekst.setOnAction(a ->saveTxt());
        btn_zapisz_szyfr_z_okienka.setOnAction(a -> saveSzyfr());
        btnWczytajSzyfr.setOnAction(a -> {
            try {
                loadFile2();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        btn_zaszyfruj_plik_binarny.setOnAction(a -> {
            try {
                szyfrujBinarnyPlik();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btn_odszyfruj_binarny.setOnAction(a -> {
            try {
                odszyfrujPlikBinarny();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void generateKeys() {
        ElGamal elGamal = new ElGamal();
        elGamal.generateKeys();
        publicKeyP.setText(HexFormat.of().formatHex( convertHexStringToBigInt(elGamal.getPrivateKey()).toByteArray()));
        publicKeyP.setText(elGamal.getpKey());
        publicKeyH.setText(elGamal.gethKey());
        publicKeyG.setText(elGamal.getgKey());
        privateKey.setText(elGamal.getPrivateKey());
    }



    public boolean verifyKeys() {
        if(publicKeyP.getText() == "" || publicKeyH.getText() == "" || publicKeyG.getText() == "" || privateKey.getText() =="") {
            infoLabel.setText("Zadne pole nie moze byc puste");
            return false;
        }
        ElGamal elGamal = new ElGamal();
        elGamal.setP(convertHexStringToBigInt(publicKeyP.getText()));
        elGamal.setH(convertHexStringToBigInt(publicKeyH.getText()));
        elGamal.setPrivateKey(convertHexStringToBigInt(privateKey.getText()));
        elGamal.setG(convertHexStringToBigInt(publicKeyG.getText()));
        if(!elGamal.verifyKeys()){
            infoLabel.setText("Niepoprawne klucze");
            return false;
        }
        return true;
    }
    public void loadFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String pom = selectedFile.getAbsolutePath();
            plikOdczytuTekstu = new File(pom);
            tekst = pliczek.wczytajZpliku(pom);
            textToEncrypt.setText(new String(tekst));

        }
    }
    public void loadFile2() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String pom = selectedFile.getAbsolutePath();
            plikOdczytuTekstu = new File(pom);
            tekst = pliczek.wczytajZpliku(pom);
            textToDecrypt.setText(new String(tekst));

        }
    }
    public void encryptMessage() {

        if(!verifyKeys()) {
            return;
        }
        content = textToEncrypt.getText().getBytes(StandardCharsets.UTF_8);
        ElGamal elGamal = new ElGamal();
        elGamal.setP(new BigInteger(HexFormat.of().parseHex(publicKeyP.getText())));
        elGamal.setH(new BigInteger(HexFormat.of().parseHex(publicKeyH.getText())));
        elGamal.setG(new BigInteger(HexFormat.of().parseHex(publicKeyG.getText())));
        String[] parts = elGamal.encryptMessage(content);
        String partsCombined = parts[0] + "\n" + parts[1];
        textToDecrypt.setText(partsCombined);

    }

    public void decryptMessage() {
        if(!verifyKeys()) {
            return;
        }
        content = textToDecrypt.getText().getBytes();
        ElGamal elGamal = new ElGamal();
        elGamal.setP(new BigInteger(HexFormat.of().parseHex(publicKeyP.getText())));
        elGamal.setPrivateKey(new BigInteger(HexFormat.of().parseHex(privateKey.getText())));
        String[] parts = textToDecrypt.getText().split("\n");
        elGamal.setC1(new BigInteger(parts[0]));
        elGamal.setC2(new BigInteger(parts[1]));
        textToEncrypt.setText(new String(elGamal.decryptMessage()));
    }

    public void szyfrujBinarnyPlik () throws IOException {
        if(!verifyKeys()) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik");
        File file2 = fileChooser.showOpenDialog(stage);
        if(file2!=null && file2.getPath()!=null) {
            content = Files.readAllBytes(Paths.get(file2.getPath()));
            ElGamal elGamal = new ElGamal();
            elGamal.setP(new BigInteger(HexFormat.of().parseHex(publicKeyP.getText())));
            elGamal.setH(new BigInteger(HexFormat.of().parseHex(publicKeyH.getText())));
            elGamal.setG(new BigInteger(HexFormat.of().parseHex(publicKeyG.getText())));
            String[] parts = elGamal.encryptMessage(content);
            String partsCombined = parts[0] + "\n" + parts[1];
            content = partsCombined.getBytes(StandardCharsets.UTF_8);
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(pdfFilter);
            FileChooser.ExtensionFilter binFilter = new FileChooser.ExtensionFilter("Binary Files (*.bin)", "*.bin");
            fileChooser.getExtensionFilters().add(binFilter);
            FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(pngFilter);
            fileChooser.setTitle("Zapisz plik");
            file = fileChooser.showSaveDialog(stage);
            try(FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void odszyfrujPlikBinarny() throws IOException {
        if(!verifyKeys()) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Proszę wybrać plik");
        File file2 = fileChooser.showOpenDialog(stage);
        if(file2!=null && file2.getPath()!=null) {
            content = Files.readAllBytes(Paths.get(file2.getPath()));
            ElGamal elGamal = new ElGamal();
            elGamal.setP(new BigInteger(HexFormat.of().parseHex(publicKeyP.getText())));
            elGamal.setPrivateKey(new BigInteger(HexFormat.of().parseHex(privateKey.getText())));
            String[] parts = new String(content).split("\n");
            elGamal.setC1(new BigInteger(parts[0]));
            elGamal.setC2(new BigInteger(parts[1]));
            content = elGamal.decryptMessage();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(pdfFilter);
            FileChooser.ExtensionFilter binFilter = new FileChooser.ExtensionFilter("Binary Files (*.bin)", "*.bin");
            fileChooser.getExtensionFilters().add(binFilter);
            FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(pngFilter);
            fileChooser.setTitle("Zapisz plik");
            file = fileChooser.showSaveDialog(stage);
            try(FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }




        }
    }
    public void saveFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);
        FileChooser.ExtensionFilter binFilter = new FileChooser.ExtensionFilter("Binary Files (*.bin)", "*.bin");
        fileChooser.getExtensionFilters().add(binFilter);
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.setTitle("Zapisz plik");
        file = fileChooser.showSaveDialog(stage);
        try(FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public BigInteger convertHexStringToBigInt(String text){
        return new BigInteger(text,16);
    }

    public void saveTxt() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Zapisz plik");
        file = fileChooser.showSaveDialog(stage);
        try(FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(textToEncrypt.getText().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void saveSzyfr() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Zapisz plik");
        file = fileChooser.showSaveDialog(stage);
        try(FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(textToDecrypt.getText().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void btnPlikBinarny(ActionEvent event) {


    }

    @FXML
    void btnWczytajPlikTekst(ActionEvent event) {

    }

    @FXML
    void btnWczytajSzyfrPlik(ActionEvent event) {

    }
    @FXML
    void btnZapiszPlikTekst(ActionEvent event) {


    }
}