package com.example.elgamal;


import javax.swing.*;
import java.lang.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.ByteArrayOutputStream;



public class ElGamal {
    class ElGamalKeyException extends Exception
    {public ElGamalKeyException(String msg){super(msg);};
    }

    public static final int BOARD_SIZE = 4;
    public static final int LENGTH_OF_TEXT = 16;
    public int padding = 0;

    AlgorithmOperations algorithmOperations = new AlgorithmOperations();

    BigInteger g,a,h,r,N,Nm1;
    int keyLen=512; //ta wartość daje długość a=512
    int ilZnHex=keyLen/4;//ilość znaków hex wyświetlanych w polu klucza

    /**
     * Metoda generująca klucz szyfrujący.
     */
    public void generateKey() {
        // Wygenerowanie liczby pierwszej o długości keyLen + 2 bitów
        N = BigInteger.probablePrime(keyLen + 2, new Random());
        // Wygenerowanie losowej liczby a o długości keyLen bitów
        a = new BigInteger(keyLen, new Random());
        // Wygenerowanie losowej liczby g o długości keyLen bitów
        g = new BigInteger(keyLen, new Random());
        // Obliczenie wartości h, która jest resztą z dzielenia g^a przez N
        h = g.modPow(a, N);
        // Obliczenie wartości N-1
        Nm1 = N.subtract(BigInteger.ONE);
    }
    private byte[] convertToOneDimensionalArray(byte[][] array) {
        byte[] oneDimensionalArray = new byte[4 * 4];
        int counter = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                oneDimensionalArray[counter++] = array[j][i];
            }
        }
        return oneDimensionalArray;
    }

    public BigInteger[][] encrypt(byte[][] dataBlock) {
        // generujemy nowe r dla każdego szyfrowania
        r = BigInteger.probablePrime(keyLen, new Random());
        while (true) {
            if (r.gcd(Nm1).equals(BigInteger.ONE)) break;
            else r = r.nextProbablePrime();
        }

        BigInteger[][] cipher = new BigInteger[dataBlock.length][2];

        for (int i = 0; i < dataBlock.length; i++) {
            byte[] block = dataBlock[i];

            cipher[i][1] = g.modPow(r, N); // C1

            BigInteger plaintext = new BigInteger(1, block);
            cipher[i][0] = plaintext.multiply(h.modPow(r, N)).mod(N); // C2
        }

        return cipher;
    }
    public ArrayList<Pair<BigInteger, Integer>> prepareForEncryption(BigInteger originalMessage) {
        String originalMessageToString = originalMessage.toString(); // kopia, żeby przypadkiem nie zepsuć
        int originalMessageLength = originalMessageToString.length();
        int i = 0;
        int index = 0; // index w zwracanej tablicy
        ArrayList<Pair<BigInteger, Integer>> cutMessage = new ArrayList<>();
        while (true) {
            cutMessage.add(new Pair<>(BigInteger.valueOf(0), 0)); // z każdą iteracją tablica musi być rozszerzana
            int numberOfInitialZeroes = 0;
            while (i < originalMessageLength && originalMessageToString.charAt(i) == '0') {
                numberOfInitialZeroes++;
                i++;
            }
            while (cutMessage.get(index).first.multiply(BigInteger.TEN).compareTo(this.N) < 0 && i < originalMessageLength) {
                cutMessage.set(index, new Pair<>(cutMessage.get(index).first.multiply(BigInteger.TEN), numberOfInitialZeroes));
                cutMessage.set(index, new Pair<>(cutMessage.get(index).first.add(BigInteger.valueOf(originalMessageToString.charAt(i) - '0')), numberOfInitialZeroes)); // '0' - 48 w ASCII
                i++;
            }
            index++;
            if (i == originalMessageLength) {
                return cutMessage;
            }
        }
    }
    public BigInteger encryptElGamal(BigInteger originalData) {
        ArrayList<Pair<BigInteger, Integer>> preparedData = prepareForEncryption(originalData);

        BigInteger encryptedData = BigInteger.ZERO;

        for (Pair<BigInteger, Integer> pair : preparedData) {
            BigInteger k = new BigInteger(keyLen, new Random()); // Losowa liczba k o długości keyLen bitów
            BigInteger c1 = g.modPow(k, N);
            BigInteger c2 = pair.first.multiply(h.modPow(k, N)).mod(N);

            // Szyfrowane dane są złączone w postaci liczby BigInteger
            encryptedData = encryptedData.multiply(N.pow(pair.second + 1)).add(c1).multiply(N).add(c2);
        }

        return encryptedData;
    }









    public BigInteger[] encrypt(String message)
    {   //generujemy nowe r dla każdego szyfrowania
        r = BigInteger.probablePrime(keyLen,new Random());
        while(true)
            if (r.gcd(Nm1).equals(BigInteger.ONE))break;
            else r=r.nextProbablePrime();
        int ileZnakow = (N.bitLength()-1)/8;
        StringBuilder messageBuilder = new StringBuilder(message);
        while (messageBuilder.length() % ileZnakow != 0)
            messageBuilder.append(' ');
        message = messageBuilder.toString();
        int chunks = message.length()/ ileZnakow;
        BigInteger[] cipher = new BigInteger[chunks*2];
        for (int i = 0,j=0; i < chunks; i++,j+=2)
        {
            String s = message.substring(ileZnakow*i,ileZnakow*(i+1));
            cipher[j] = AlgorithmOperations.stringToBigInteger(s);
            cipher[j] = cipher[j].multiply(h.modPow(r,N)).mod(N);//C2
            cipher[j+1] = g.modPow(r,N);//C1
        }
        return cipher;
    }

    /**

     Metoda służąca do szyfrowania wiadomości podanej jako String i zwracająca szyfrogram jako String.
     @param message wiadomość do zaszyfrowania
     @return szyfrogram w postaci String
     */
    public String encryptFromStringToString(String message)
    {
        StringBuilder str = new StringBuilder();
        BigInteger[] bi_table = encrypt(message); //szyfrowanie wiadomości
        for (BigInteger bigInteger : bi_table)
            str.append(bigInteger).append("\n"); //łączenie poszczególnych części szyfrogramu w jeden String z separatorem "\n"
        return str.toString();
    }
    /**

     Metoda służąca do odszyfrowywania wiadomości na podstawie szyfrogramu.
     @param cipher szyfrogram w postaci tablicy BigInteger'ów
     @return oryginalna wiadomość w postaci String
     */
    public String decrypt(BigInteger[] cipher)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < cipher.length; i+=2)
        {
            //Odszyfrowanie pojedynczej części szyfrogramu i dodanie wyniku do Stringa s
            s.append(AlgorithmOperations.bigIntegerToString(cipher[i].multiply(cipher[i + 1].modPow(a, N).modInverse(N)).mod(N)));
        }
        return s.toString();
    }

    /**
     -decryptToBigInt() - metoda deszyfrująca tablicę cipher na tablicę wynik zwracającą obiekty klasy BigInteger.
     -len jako długość tablicy wynikowej (zakładając, że cipher ma parzystą długość).
     -W decryptToBigInt() każda kolejna para liczb w tablicy cipher jest deszyfrowana zgodnie z algorytmem ElGamala i zapisywana w tablicy wynikowej wynik.
     */
    public BigInteger[] decryptToBigInt(BigInteger[] cipher)
    {
        int len = cipher.length / 2;
        BigInteger[] wynik = new BigInteger[len];

        for (int i = 0, j = 0; i < len; i++, j += 2) {
            wynik[i] = cipher[j].multiply(cipher[j + 1].modPow(a, N).modInverse(N)).mod(N);
        }

        return wynik;
    }

    public byte[][] distinguishData(byte[] data) {
        byte[][] dataArraysHolder;

        if(data.length%16 == 0) {
            dataArraysHolder= new byte[(data.length/16)][LENGTH_OF_TEXT];
        }
        else {
            dataArraysHolder= new byte[(data.length/16)+1][LENGTH_OF_TEXT];
        }
        int counter = 0;
        for(int i=0; i<dataArraysHolder.length; i++) {
            for(int j=0; j<LENGTH_OF_TEXT; j++) {
                if(counter >= data.length) {
                    dataArraysHolder[i][j] = (byte) 0;
                    padding++;
                }
                else {
                    dataArraysHolder[i][j] = data[counter];
                }
                counter++;
            }
        }
        return dataArraysHolder;
    }










    /**
     -decryptFromStringToString() - metoda deszyfrująca łańcuch znaków cipher na łańcuch znaków odszyfrowanej wiadomości.
     -len jako długość tablicy wynikowej (zakładając, że cipher ma parzystą długość).
     -W decryptFromStringToString() najpierw następuje parsowanie łańcucha cipher do tablicy bi_table, a następnie deszyfrowanie tej tablicy przy użyciu metody decrypt().

     */
    public String decryptFromStringToString(String cipher)
    {
        String[] wiersze = cipher.split("\n");
        BigInteger[] bi_table = new BigInteger[wiersze.length];

        for (int i = 0; i < wiersze.length; i++) {
            bi_table[i] = new BigInteger(wiersze[i]);
        }

        return decrypt(bi_table);
    }

    public static BigInteger[] toBigIntegerArray(byte[] array) {
        BigInteger[] result = new BigInteger[array.length / 2 + array.length % 2];

        for (int i = 0; i < result.length; i++) {
            byte[] subArray = Arrays.copyOfRange(array, i * 2, Math.min(i * 2 + 2, array.length));
            result[i] = new BigInteger(subArray);
        }

        return result;
    }
    public byte[][] transformToByteMatrix(byte[] byteText) {
        byte[][] bytes = new byte[4][4];
        int plainTextByteCounter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (plainTextByteCounter >= byteText.length) {
                    bytes[j][i] = 0; // padding
                } else {
                    bytes[j][i] = byteText[plainTextByteCounter];
                }
                plainTextByteCounter++;
            }
        }
        return bytes;
    }




}