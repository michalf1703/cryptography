package com.example.elgamal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

public class ElGamalController {

    FileOperations pliczek = new FileOperations();
    ElGamal elGamal = new ElGamal();
    private FileChooser fileChooser = new FileChooser();
    AlgorithmOperations algorithmOperations = new AlgorithmOperations();
    private byte tekst[];
    EncryptedData encryptedMessageAsList = new EncryptedData();
    private byte[] loadedFileContent;
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
    private byte deszyfr_binarka[];
    BigInteger dataHolder = null;
    boolean isNegative = false;
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
                if(textField1.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za krótka.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField1.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za długa.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za krótka.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za długa.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField4.getText().length()<elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za krótka.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");
                if(textField4.getText().length()>elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za długa.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");

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
    void btnOdszyfrujPlikBinarny(ActionEvent event) throws Exception {

        try{
            if(reczna_edycja_klucza)
            {
                if(textField1.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za krótka.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField1.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za długa.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za krótka.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za długa.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField4.getText().length()<elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za krótka.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");
                if(textField4.getText().length()>elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za długa.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");

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

            else {

            }

        } catch(ElGamal.ElGamalKeyException e){JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
        catch(NumberFormatException  e1){JOptionPane.showMessageDialog(null, "Wartość klucza musi być podana w systemie szesnastkowym!", "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
        catch(Exception e2){JOptionPane.showMessageDialog(null, e2.getMessage(), "Wybierz plik", JOptionPane.ERROR_MESSAGE); }





    }






    @FXML
    void btnPlikBinarny(ActionEvent event) throws IOException {
        try{
            if(reczna_edycja_klucza)
            {
                if(textField1.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za krótka.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField1.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za długa.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za krótka.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za długa.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField4.getText().length()<elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za krótka.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");
                if(textField4.getText().length()>elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za długa.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");

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

            else {

            }

        } catch(ElGamal.ElGamalKeyException e){JOptionPane.showMessageDialog(null, e.getMessage(), "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
        catch(NumberFormatException  e1){JOptionPane.showMessageDialog(null, "Wartość klucza musi być podana w systemie szesnastkowym!", "Problem z kluczem", JOptionPane.ERROR_MESSAGE); }
        catch(Exception e2){JOptionPane.showMessageDialog(null, e2.getMessage(), "Wybierz plik", JOptionPane.ERROR_MESSAGE); }



    }



///////////////
    @FXML
    void btnSzyfrowanie(ActionEvent event) {
        try{
            if(reczna_edycja_klucza)
            {
                if(textField1.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za krótka.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField1.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość g jest za długa.\nWynosi "+ textField1.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()<elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za krótka.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField3.getText().length()>elGamal.ilZnHex)throw elGamal.new ElGamalKeyException("Podana wartość a jest za długa.\nWynosi "+ textField3.getText().length()+" .\nMusi wynosić "+ elGamal.ilZnHex+" znaki w systemie szesnastkowym.");
                if(textField4.getText().length()<elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za krótka.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");
                if(textField4.getText().length()>elGamal.ilZnHex+1)throw elGamal.new ElGamalKeyException("Podana wartość N jest za długa.\nWynosi "+ textField4.getText().length()+" .\nMusi wynosić "+ (elGamal.ilZnHex+1)+" znaków w systemie szesnastkowym.");

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

    //////////////////////
    protected void LoadFromFileDecryptedData() {
        String text;
        try {
            Path path = Paths.get("C:\\Users\\Hp\\Documents\\GitHub\\cryptography\\ElGamal\\" + "zakodowane.pdf");
            //Path path = Paths.get("C:\\Users\\cybul\\Downloads\\" + fileNameTextField1.getText());
            loadedFileContent = Files.readAllBytes(path); //pracujemy na loadedFileContent
            BigInteger fileContentAsDigits = new BigInteger(loadedFileContent);
            char[] fileContentInAscii = new char[loadedFileContent.length];
            for (int i = 0; i < loadedFileContent.length; i++) {
                fileContentInAscii[i] = (char) loadedFileContent[i];
            }
            
            text = fileContentAsDigits.toString();
            szyfruj_plik.setText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void LoadFromFileEncryptedData() {
        try {
            Path path = Paths.get("C:\\Users\\Hp\\Documents\\GitHub\\cryptography\\ElGamal\\" + "zakodowane.pdf");
            //Path path = Paths.get("C:\\Users\\cybul\\Downloads\\" + fileNameTextField1.getText());
            FileInputStream fi = new FileInputStream(path.toString());
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            encryptedMessageAsList = (EncryptedData) oi.readObject(); //odczytujemy encrptedMessageAsList
            isNegative = encryptedMessageAsList.isNegative();
            oi.close();
            fi.close();
            String message = new String();
            String zero = new String("0");
            for (int i = 0; i < encryptedMessageAsList.getSize(); i++) {
                message += zero.repeat(encryptedMessageAsList.getContent(i).second);
                message += encryptedMessageAsList.getContent(i).first.toString();
            }
            //dataHolder = new BigInteger(String.valueOf(message));
            //String readyText = new String(dataHolder.toByteArray());
            wczytaj_tekst.setText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void SaveToFileDecryptedData() {
        try {
                Path path;
                path = Paths.get("C:\\Users\\Hp\\Documents\\GitHub\\cryptography\\ElGamal\\" + "zakodowane.pdf");


            if(isNegative) {
                byte []dataToFile = new byte[dataHolder.toByteArray().length+1];
                for(int i=1; i<dataToFile.length; i++) {
                    dataToFile[i] = dataHolder.toByteArray()[i-1];
                }
                dataToFile[0] = (byte) 0xff;
                Files.write(path, dataToFile);
            }
            else {
                Files.write(path, dataHolder.toByteArray()); //tu bedzie blad!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveToFileEncryptedData() {
        try {
            Path path;
                path = Paths.get("C:\\Users\\Hp\\Documents\\GitHub\\cryptography\\ElGamal\\" + "zakodowane.pdf");


            FileOutputStream fileOutputStream
                    = new FileOutputStream(path.toString());
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(encryptedMessageAsList); //musimy zapisac encryptedMessageAsList
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

}