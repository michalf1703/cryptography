package com.example.elgamal;
import javax.swing.*;
import java.lang.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.security.*;

public class ElGamal {
    class ElGamalKeyException extends Exception
    {public ElGamalKeyException(String msg){super(msg);};
    }

    AlgorithmOperations algorithmOperations = new AlgorithmOperations();

    BigInteger g,a,h,r,rm1,N,Nm1;
    MessageDigest digest;
    MillerRabin test_pierwszosci;
    int keyLen=512; //ta wartość daje długość a=512
    int ilZnHex=keyLen/4;//ilość znaków hex wyświetlanych w polu klucza
    Random random=new Random();

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


    public BigInteger[] encryptToBigInt(byte[] message) {

        // generujemy nowe r dla każdego szyfrowania
        r = BigInteger.probablePrime(keyLen, new Random());
        if(test_pierwszosci.isPrime(r)== true ) {

            while (true) {
                if (r.gcd(Nm1).equals(BigInteger.ONE)) {
                    break;
                } else {
                    r = r.nextProbablePrime();
                }
            }
        }
        else  {JOptionPane.showMessageDialog(null, "Wartość r nie jest liczba pierwsza!", "Problem z wylosowanym r", JOptionPane.ERROR_MESSAGE);}
            // ustalamy ile znaków będzie w jednym bloku
            int ileZnakow = (a.bitLength() - 1) / 8;

            // sprawdzamy czy wiadomość jest podzielna przez ileZnakow
            boolean reszta = false;
            int chunks = 0;
            if (message.length % ileZnakow != 0) {
                chunks = (message.length / ileZnakow) + 1;
                reszta = true;
            } else {
                chunks = message.length / ileZnakow;
            }

            // inicjalizujemy tablicę na zaszyfrowane bloki
            BigInteger[] cipher = new BigInteger[chunks * 2];

            // dzielimy wiadomość na bloki i szyfrujemy każdy z nich
            if (!reszta) {
                for (int i = 0, j = 0; i < chunks; i++, j += 2) {
                    byte[] pom = algorithmOperations.getSubarray(message, ileZnakow * i, ileZnakow * (i + 1));
                    cipher[j] = new BigInteger(1, pom);
                    cipher[j] = cipher[j].multiply(h.modPow(r, N)).mod(N); // C2
                    cipher[j + 1] = g.modPow(r, N); // C1
                }
            } else {
                for (int i = 0, j = 0; i < chunks - 1; i++, j += 2) {
                    byte[] pom = algorithmOperations.getSubarray(message, ileZnakow * i, ileZnakow * (i + 1));
                    cipher[j] = new BigInteger(1, pom);
                    cipher[j] = cipher[j].multiply(h.modPow(r, N)).mod(N); // C2
                    cipher[j + 1] = g.modPow(r, N); // C1
                }
                byte[] pom = algorithmOperations.getSubarray(message, ileZnakow * (chunks - 1), message.length);
                cipher[(chunks - 1) * 2] = new BigInteger(1, pom);
                cipher[(chunks - 1) * 2] = cipher[(chunks - 1) * 2].multiply(h.modPow(r, N)).mod(N); // C2
                cipher[((chunks - 1) * 2) + 1] = g.modPow(r, N); // C1
            }


        return cipher;

    }
    /**

     Metoda służąca do szyfrowania wiadomości.

     @param message wiadomość do zaszyfrowania.

     @return zaszyfrowana wiadomość w postaci tablicy BigIntegers.
     */

    public BigInteger[] encrypt(String message)
    {   //generujemy nowe r dla każdego szyfrowania
        r = BigInteger.probablePrime(keyLen,new Random());
        while(true)
            if (r.gcd(Nm1).equals(BigInteger.ONE))break;
            else r=r.nextProbablePrime();
        int ileZnakow = (N.bitLength()-1)/8;
        while (message.length() % ileZnakow != 0)
            message += ' ';
        int chunks = message.length()/ ileZnakow;
        BigInteger[] cipher = new BigInteger[chunks*2];
        for (int i = 0,j=0; i < chunks; i++,j+=2)
        {
            String s = message.substring(ileZnakow*i,ileZnakow*(i+1));
            cipher[j] = algorithmOperations.stringToBigInteger(s);
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
        String str = new String();
        BigInteger[] bi_table = encrypt(message); //szyfrowanie wiadomości
        for(int i = 0; i < bi_table.length; i++)
            str += bi_table[i] + "\n"; //łączenie poszczególnych części szyfrogramu w jeden String z separatorem "\n"
        return str;
    }
    /**

     Metoda służąca do odszyfrowywania wiadomości na podstawie szyfrogramu.
     @param cipher szyfrogram w postaci tablicy BigInteger'ów
     @return oryginalna wiadomość w postaci String
     */
    public String decrypt(BigInteger[] cipher)
    {
        String s = new String();
        for (int i = 0; i < cipher.length; i+=2)
        {
        //Odszyfrowanie pojedynczej części szyfrogramu i dodanie wyniku do Stringa s
            s += algorithmOperations.bigIntegerToString(cipher[i].multiply(cipher[i+1].modPow(a, N).modInverse(N)).mod(N));
        }
        return s;
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
