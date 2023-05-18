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


    public BigInteger decrypt(BigInteger data) {
        return data.modPow(a, N).modInverse(N);
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


}