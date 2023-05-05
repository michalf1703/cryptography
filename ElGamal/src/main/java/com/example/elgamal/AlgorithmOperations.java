package com.example.elgamal;

import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AlgorithmOperations {
    /**
     * Zwraca podtablicę bajtów z danymi z danego przedziału.
     *
     * @param data      oryginalna tablica bajtów
     * @param start  indeks początkowy (włącznie) przedziału, z którego chcemy utworzyć podtablicę
     * @param stop    indeks końcowy (wyłącznie) przedziału, z którego chcemy utworzyć podtablicę
     * @return          podtablica bajtów z danymi z danego przedziału
     */
    public static byte[] getSubarray(byte data[], int start, int stop)
    {
        byte[] subArray = new byte[stop-start];
        for(int i = 0; start < stop; start++, i++) {
            subArray[i] = data[start];
        }
        return subArray;
    }

    /**
     * Konwertuje dany ciąg znaków na liczbę BigInteger.
     *
     * @param str   ciąg znaków, który chcemy skonwertować na liczbę BigInteger
     * @return      liczba BigInteger, reprezentująca dany ciąg znaków
     */
    public static BigInteger stringToBigInteger(String str)
    {
        byte[] tab = new byte[str.length()];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = (byte)str.charAt(i);
        }
        return new BigInteger(1, tab);
    }

    //konwertuje BigIntegera na string
    /**
     * Konwertuje liczbę BigInteger na ciąg znaków.
     *
     * @param n     liczba BigInteger, którą chcemy skonwertować na ciąg znaków
     * @return      ciąg znaków reprezentujący daną liczbę BigInteger
     */
    public static String bigIntegerToString(BigInteger n)
    {
        byte[] bytes = n.toByteArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append((char) bytes[i]);
        }
        return sb.toString();
    }
    public static String convertToString(BigInteger[] bigIntegers) {
        String result = Arrays.stream(bigIntegers)
                .map(BigInteger::toString)
                .reduce("", (s1, s2) -> s1 + s2);
        return result;
    }

    /**
     * Konwertuje tablicę bajtów na ciąg znaków w systemie heksadecymalnym.
     *
     * @param bytes     tablica bajtów, którą chcemy skonwertować na ciąg znaków w systemie heksadecymalnym
     * @return          ciąg znaków reprezentujący daną tablicę bajtów w systemie heksadecymalnym
     */
    public static String bytesToHex(byte[] bytes)
    {
        StringBuilder hexText = new StringBuilder();

        for (int i = 0; i < bytes.length; i++)
        {
            // Konwertuj bajt na wartość dodatnią
            int positiveValue = bytes[i] & 0x000000FF;

            // Konwertuj wartość dodatnią na wartość w systemie heksadecymalnym
            String hexValue = Integer.toHexString(positiveValue);

            // Jeśli wartość heksadecymalna ma tylko jedną cyfrę, dodaj zero na początek
            if (hexValue.length() < 2)
            {
                hexText.append("0");
            }

            // Dodaj wartość heksadecymalną do ciągu wynikowego
            hexText.append(hexValue);
        }

        return hexText.toString();
    }
    public static String bytesToHex2(byte[] bytes)
    {
        StringBuilder hexText = new StringBuilder();

        for (int i = 0; i < bytes.length; i++)
        {
            // Konwertuj bajt na wartość dodatnią
            int positiveValue = bytes[i] & 0x000000FF;

            // Konwertuj wartość dodatnią na wartość w systemie heksadecymalnym
            String hexValue = Integer.toHexString(positiveValue);

            // Jeśli wartość heksadecymalna ma tylko jedną cyfrę, dodaj zero na początek
            if (hexValue.length() < 2)
            {
                hexText.append("0");
            }

            // Dodaj wartość heksadecymalną do ciągu wynikowego
            hexText.append(hexValue);
        }

        return hexText.toString();
    }

    /**
     * Metoda konwertująca string w formacie HEX na tablicę bajtów.
     *
     * @param tekst string w formacie HEX
     * @return tablica bajtów
     */
    public static byte[] hexToBytes(String tekst) {
        // Sprawdzenie, czy tekst nie jest null lub jest zbyt krótki
        if (tekst == null) {
            return null;
        } else if (tekst.length() < 2) {
            return null;
        } else {
            // Jeśli długość tekstu jest nieparzysta, dodajemy "0" na końcu, aby dopełnić parzystą liczbę cyfr
            if (tekst.length() % 2 != 0) {
                tekst += '0';
            }
            // Obliczenie długości tablicy bajtów
            int dl = tekst.length() / 2;
            // Utworzenie tablicy bajtów
            byte[] wynik = new byte[dl];
            // Konwersja każdej pary cyfr HEX na odpowiadający jej bajt
            for (int i = 0; i < dl; i++) {
                try {
                    // Wykorzystanie metody parseInt do konwersji cyfr HEX na bajt
                    wynik[i] = (byte) Integer.parseInt(tekst.substring(i * 2, i * 2 + 2), 16);
                } catch (NumberFormatException e) {
                    // W przypadku błędu wyświetlenie komunikatu o błędzie
                    JOptionPane.showMessageDialog(null, "Problem z przekonwertowaniem HEX->BYTE.\n Sprawdź wprowadzone dane.", "Problem z przekonwertowaniem HEX->BYTE", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Zwrócenie tablicy bajtów
            return wynik;
        }
    }

    //zwraca podtablice z elementami od..do z podanej tablicy
    public static byte[] podtablica(byte dane[], int poczatek, int koniec)
    {
        byte[] subArray = new byte[koniec-poczatek];
        for(int i =0; poczatek < koniec; poczatek++, i++) subArray[i] = dane[poczatek];
        return subArray;
    }
    public BigInteger[] readBigIntegersFromFile(String fileName) throws IOException {
        byte[] fileBytes = Files.readAllBytes(new File(fileName).toPath());
        String[] fileValues = new String(fileBytes).split("\\s+");
        BigInteger[] bigIntegers = new BigInteger[fileValues.length];
        for (int i = 0; i < fileValues.length; i++) {
            bigIntegers[i] = new BigInteger(fileValues[i]);
        }
        return bigIntegers;
    }

    //dodaje zero do tablicy bajtów
    public static byte[] dodajZero(byte dane[])
    {
        byte[] wynik = new byte[dane.length+1];
        wynik[0] = '\000';
        for(int i = 0; i < dane.length; i++) wynik[i+1] = dane[i];
        return wynik;
    }

    //zwraca tablice z podanej tablicy z powycinanymi zerami
    public static byte[] podtablicaBezZer(byte dane[])
    {
        ArrayList<Byte> tab = new ArrayList<Byte>();
        for(int i = dane.length-1; i >= 0; i--)
        {  if(dane[i] == '\000') continue;
        else tab.add(dane[i]);
        }
        byte[] wynik = new byte[tab.size()];
        for(int j = 0, i = tab.size()-1; i >= 0; i--, j++)
            wynik[j] = tab.get(i).byteValue();
        return wynik;
    }
    //zapisuje do pliku tablicę BigIntegerów
    public static void zapiszDoPlikuTabliceBigInt(BigInteger dane[], String nazwa_pliku)
    {
        byte[] tab;
        try {
            File file = new File(nazwa_pliku);
            FileOutputStream fos = new FileOutputStream(file);
            for(int i = 0; i < dane.length; i++)
            { if(dane[i].equals(BigInteger.ZERO))
            { tab = new byte[1];
                tab[0] = '\000';
                fos.write(tab);
            }
            else
            { tab = dane[i].toByteArray();
                // byteArray powinna miec długosc 31
                if(tab[0] == '\000' && tab.length == 32) tab = podtablica(tab, 1, tab.length);
                if(tab.length == 30) tab = dodajZero(tab);
                if(i == dane.length-1) // usun nulle z uzupelnienia
                    tab = podtablicaBezZer(tab);
                fos.write(tab);
            }
            }
            fos.close();
        } catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
    }
    public static String bytesConvertToString(byte[] bytes) {
        String result = new String(bytes, StandardCharsets.UTF_8);
        return result;
    }
    public static byte[] convertToByteArray(String str) {
        byte[] result = str.getBytes(StandardCharsets.UTF_8);
        return result;
    }
    public static byte[] bigIntegerToBytes(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] == 0) { // jeśli pierwszy bajt jest zerem, usuń go
            byte[] trimmedArray = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, trimmedArray, 0, trimmedArray.length);
            return trimmedArray;
        }
        return byteArray;
    }
    public static byte[] bigIntToByteArray(BigInteger[] bigIntegers) {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        DataOutputStream dataOutStream = new DataOutputStream(byteOutStream);
        try {
            for (BigInteger bi : bigIntegers) {
                byte[] byteArray = bi.toByteArray();
                dataOutStream.writeInt(byteArray.length);
                dataOutStream.write(byteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteOutStream.toByteArray();
    }


    //wczytuje dane z pliku do tabliy BigIntegerów
    public static BigInteger[] wczytajZPlikuTabliceBigInt(String nazwa_pliku) {
        List<BigInteger> list = new ArrayList<>();
        try {
            File file = new File(nazwa_pliku);
            Scanner sc = new Scanner(file);
            while (sc.hasNextBigInteger()) {
                list.add(sc.nextBigInteger());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list.toArray(new BigInteger[0]);
    }

    public static byte[] convert(BigInteger[] bigIntegers) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (BigInteger bigInteger : bigIntegers) {
            byte[] bytes = bigInteger.toByteArray();
            outputStream.write(bytes, 0, bytes.length);
        }
        return outputStream.toByteArray();
    }

    public static byte[] convertToByteArray(BigInteger[] bigIntegers) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            for (BigInteger bi : bigIntegers) {
                byte[] bytes = bi.toByteArray();
                dos.writeInt(bytes.length);
                dos.write(bytes);
            }
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static BigInteger[] convertToBigIntArray(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        List<BigInteger> bigIntegers = new ArrayList<>();
        try {
            while (dis.available() > 0) {
                byte[] biBytes = new byte[dis.readInt()];
                dis.read(biBytes);
                bigIntegers.add(new BigInteger(biBytes));
            }
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bigIntegers.toArray(new BigInteger[bigIntegers.size()]);
    }
    public static byte[] combineArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}

