package com.example.elgamal;

import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
    public static byte[] getSubarray(byte[] data, int start, int stop)
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
        for (byte aByte : bytes) {
            sb.append((char) aByte);
        }
        return sb.toString();
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

        for (byte aByte : bytes) {
            // Konwertuj bajt na wartość dodatnią
            int positiveValue = aByte & 0x000000FF;

            // Konwertuj wartość dodatnią na wartość w systemie heksadecymalnym
            String hexValue = Integer.toHexString(positiveValue);

            // Jeśli wartość heksadecymalna ma tylko jedną cyfrę, dodaj zero na początek
            if (hexValue.length() < 2) {
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

        for (byte aByte : bytes) {
            // Konwertuj bajt na wartość dodatnią
            int positiveValue = aByte & 0x000000FF;

            // Konwertuj wartość dodatnią na wartość w systemie heksadecymalnym
            String hexValue = Integer.toHexString(positiveValue);

            // Jeśli wartość heksadecymalna ma tylko jedną cyfrę, dodaj zero na początek
            if (hexValue.length() < 2) {
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
    public byte[] bigIntegerArrayToByteArray(BigInteger[] bigIntArray) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            for (BigInteger bi : bigIntArray) {
                byte[] bytes = bi.toByteArray();
                // zapisz długość bajtów jako int
                outputStream.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
                // zapisz bajty
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
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
                fos.write(tab);
            }
            else
            { tab = dane[i].toByteArray();
                // byteArray powinna miec długosc 31
                if(tab[0] == '\000' && tab.length == 32) tab = getSubarray(tab, 1, tab.length);
                if(tab.length == 30) tab = dodajZero(tab);
                if(i == dane.length-1) // usun nulle z uzupelnienia
                    tab = podtablicaBezZer(tab);
                fos.write(tab);
            }
            }
            fos.close();
        } catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
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
    //wczytuje dane z pliku do tabliy BigIntegerów
    public static BigInteger[] readBigIntegersFromFile(File file) {
        //File file = new File(fileName);
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku: ");
            e.printStackTrace();
        }

        int n = scanner.nextInt();
        BigInteger[] bigIntegers = new BigInteger[n];

        for (int i = 0; i < n; i++) {
            bigIntegers[i] = scanner.nextBigInteger();
        }

        scanner.close();

        return bigIntegers;
    }
}



