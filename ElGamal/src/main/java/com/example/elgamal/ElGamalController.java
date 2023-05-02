package com.example.elgamal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ElGamalController {

    FileOperations pliczek = new FileOperations();
    ElGamal elGamal = new ElGamal();
    private FileChooser fileChooser = new FileChooser();
    AlgorithmOperations algorithmOperations = new AlgorithmOperations();
    private byte tekst[];
    private File plikOdczytuTekstu,plikOdczytuszyfr, plikzapisuSzyfr;
    private byte[] szyfr;
    private BigInteger[] deszyfr_kolejny;
    private byte[] szyfr2,deszyfr2115;
    private BigInteger[] deszyfr;
    private BigInteger [] szyfr_plik_binarny;
    private BigInteger [] deszyfr_plik_binarny;
    private BigInteger[] plik_binarny_szyfr;
    private BigInteger[] odczytywanko;
    private BigInteger dupeczka, deszyfr123;
    private boolean reczna_edycja_klucza=false;
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
        try{
        if(reczna_edycja_klucza)
        {
            if(textField1.getText().length()<elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość g jest za krótka.\nWynosi " + textField1.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
            if(textField1.getText().length()>elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość g jest za długa.\nWynosi " + textField1.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
            if(textField3.getText().length()<elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość a jest za krótka.\nWynosi " + textField3.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
            if(textField3.getText().length()>elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość a jest za długa.\nWynosi " + textField3.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
            if(textField4.getText().length()<elGamal.ilZnHex+1)throw new ElGamal.ElGamalKeyException("Podana wartość N jest za krótka.\nWynosi " + textField4.getText().length() + " .\nMusi wynosić " + (elGamal.ilZnHex + 1) + " znaków w systemie szesnastkowym.");
            if(textField4.getText().length()>elGamal.ilZnHex+1)throw new ElGamal.ElGamalKeyException("Podana wartość N jest za długa.\nWynosi " + textField4.getText().length() + " .\nMusi wynosić " + (elGamal.ilZnHex + 1) + " znaków w systemie szesnastkowym.");

            elGamal.g= new BigInteger(textField1.getText(),16);
            elGamal.a= new BigInteger(textField3.getText(),16);
            elGamal.N= new BigInteger(textField4.getText(),16);
            elGamal.h=elGamal.g.modPow(elGamal.a,elGamal.N);
            BigInteger h=new BigInteger(textField2.getText(),16);
            if (!elGamal.h.equals(h))
            {JOptionPane.showMessageDialog(null, "Wartość h nie zgadza się z wartościami g, a oraz N!\nzostała obliczona poprawna wartość h.", "Problem z kluczem publicznym", JOptionPane.ERROR_MESSAGE);
                textField2.setText(elGamal.h.toString(16));
            }
            reczna_edycja_klucza=false;
        }

        String spom=new String(algorithmOperations.hexToBytes(szyfruj_plik.getText()));
            wczytaj_tekst.setText(elGamal.decryptFromStringToString(spom));

    } catch(ElGamal.ElGamalKeyException e){JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
    catch(NumberFormatException  e1){JOptionPane.showMessageDialog(null, "Wartość klucza musi być podana w systemie szesnastkowym!", "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }

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
    void btnOdszyfrujPlikBinarny(ActionEvent event) throws IOException {
        elGamal.g= new BigInteger(textField1.getText(),16);
        elGamal.a= new BigInteger(textField3.getText(),16);
        elGamal.N= new BigInteger(textField4.getText(),16);
        elGamal.h=elGamal.g.modPow(elGamal.a,elGamal.N);
        BigInteger h=new BigInteger(textField2.getText(),16);
        if (!elGamal.h.equals(h))
        {JOptionPane.showMessageDialog(null, "Wartość h nie zgadza się z wartościami g, a oraz N!\nzostała obliczona poprawna wartość h.", "Problem z kluczem publicznym", JOptionPane.ERROR_MESSAGE);
            textField2.setText(elGamal.h.toString(16));
        }



    }

    @FXML
    void btnPlikBinarny(ActionEvent event) throws IOException {

        File file = fileChooser.showOpenDialog(null);
        String sciezka = file.getPath();
        byte[] content = Files.readAllBytes(Paths.get(sciezka));
        elGamal.g= new BigInteger(textField1.getText(),16);
        elGamal.a= new BigInteger(textField3.getText(),16);
        elGamal.N= new BigInteger(textField4.getText(),16);
        elGamal.h=elGamal.g.modPow(elGamal.a,elGamal.N);
        BigInteger h=new BigInteger(textField2.getText(),16);
        plik_binarny_szyfr = elGamal.encryptToBigInt(content);
        //////////////////////////////////////////////
        StringBuilder sb = new StringBuilder();
        for (BigInteger num : plik_binarny_szyfr) {
            sb.append(num.toString(16));
        }
        String hexString = sb.toString();
        szyfruj_plik.setText(hexString);
        algorithmOperations.zapiszDoPlikuTabliceBigInt(plik_binarny_szyfr,"zakodowane");


    }

    @FXML
    void btnSzyfrowanie(ActionEvent event) {
        try{
            if(reczna_edycja_klucza)
            {
                if(textField1.getText().length()<elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość g jest za krótka.\nWynosi " + textField1.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
                if(textField1.getText().length()>elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość g jest za długa.\nWynosi " + textField1.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
                if(textField3.getText().length()<elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość a jest za krótka.\nWynosi " + textField3.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
                if(textField3.getText().length()>elGamal.ilZnHex)throw new ElGamal.ElGamalKeyException("Podana wartość a jest za długa.\nWynosi " + textField3.getText().length() + " .\nMusi wynosić " + elGamal.ilZnHex + " znaki w systemie szesnastkowym.");
                if(textField4.getText().length()<elGamal.ilZnHex+1)throw new ElGamal.ElGamalKeyException("Podana wartość N jest za krótka.\nWynosi " + textField4.getText().length() + " .\nMusi wynosić " + (elGamal.ilZnHex + 1) + " znaków w systemie szesnastkowym.");
                if(textField4.getText().length()>elGamal.ilZnHex+1)throw new ElGamal.ElGamalKeyException("Podana wartość N jest za długa.\nWynosi " + textField4.getText().length() + " .\nMusi wynosić " + (elGamal.ilZnHex + 1) + " znaków w systemie szesnastkowym.");

                elGamal.g= new BigInteger(textField1.getText(),16);
                elGamal.a= new BigInteger(textField3.getText(),16);
                elGamal.N= new BigInteger(textField4.getText(),16);
                elGamal.h=elGamal.g.modPow(elGamal.a,elGamal.N);
                BigInteger h=new BigInteger(textField2.getText(),16);
                if (!elGamal.h.equals(h))
                {JOptionPane.showMessageDialog(null, "Wartość h nie zgadza się z wartościami g, a oraz N!\nzostała obliczona poprawna wartość h.", "Problem z kluczem publicznym", JOptionPane.ERROR_MESSAGE);
                    textField2.setText(elGamal.h.toString(16));
                }
                reczna_edycja_klucza=false;
            }

            else szyfruj_plik.setText(algorithmOperations.bytesToHex(elGamal.encryptFromStringToString(wczytaj_tekst.getText()).getBytes()));

        } catch(ElGamal.ElGamalKeyException e){JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
        catch(NumberFormatException  e1){JOptionPane.showMessageDialog(null, "Wartość klucza musi być podana w systemie szesnastkowym!", "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
        catch(Exception e2){JOptionPane.showMessageDialog(null, e2.getMessage(), "Wybierz plik", JOptionPane.ERROR_MESSAGE); }

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
    void btnZapiszPlikTekst(ActionEvent event) {
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
    void btnZapiszSzyfr(ActionEvent event) {
        FileDialog fd = new FileDialog((Frame)null, "Zapisz plik", FileDialog.SAVE);
        fd.setVisible(true);
        String fileName = fd.getFile();
        String directory = fd.getDirectory();
        if (fileName != null && directory != null) {
            String path = directory + fileName;
            try {
                pliczek.zapiszDoPliku(szyfruj_plik.getText().getBytes(), path);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Problem z zapisem do pliku " + path, "Problem z zapisem do pliku", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}
