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
    int keyLen=512; //ta wartość daje długość a=512
    int ilZnHex=keyLen/4;//ilość znaków hex wyświetlanych w polu klucza
    Random random=new Random();

    public void generateKey()
    {
        N = BigInteger.probablePrime(keyLen+2,new Random());
        a = new BigInteger(keyLen,new Random());
        g = new BigInteger(keyLen,new Random());
        h=g.modPow(a,N);
        Nm1=N.subtract(BigInteger.ONE);
    }

    public BigInteger[] encrypt(byte[] message)
    {      //generujemy nowe r dla każdego szyfrowania
        r = BigInteger.probablePrime(keyLen,new Random());
        while(true)
            if (r.gcd(Nm1).equals(BigInteger.ONE))break;
            else r=r.nextProbablePrime();
        int ileZnakow = (a.bitLength()-1)/8;
        boolean reszta=false;
        int chunks=0;
        if(message.length % ileZnakow != 0){chunks = (message.length/ ileZnakow)+1;reszta=true;}
        else chunks = message.length/ ileZnakow;
        BigInteger[] cipher = new BigInteger[chunks*2];
        if(!reszta)
        {
            for (int i = 0, j=0; i < chunks; i++,j+=2)
            {
                byte[] pom = algorithmOperations.podtablica(message, ileZnakow*i, ileZnakow*(i+1));
                cipher[j] = new BigInteger(1, pom);
                cipher[j] = cipher[j].multiply(h.modPow(r,N)).mod(N);//C2
                cipher[j+1] = g.modPow(r,N);//C1
            }
        }
        else
        {
            for (int i = 0, j=0; i < chunks-1; i++,j+=2)
            {
                byte[] pom = algorithmOperations.podtablica(message, ileZnakow*i, ileZnakow*(i+1));
                cipher[j] = new BigInteger(1, pom);
                cipher[j] = cipher[j].multiply(h.modPow(r,N)).mod(N);//C2
                cipher[j+1] = g.modPow(r,N);//C1
            }
            byte[] pom = algorithmOperations.podtablica(message, ileZnakow*(chunks-1), message.length);
            cipher[(chunks-1)*2] = new BigInteger(1, pom);
            cipher[(chunks-1)*2] = cipher[(chunks-1)*2].multiply(h.modPow(r,N)).mod(N);//C2
            cipher[((chunks-1)*2)+1] = g.modPow(r,N);//C1
        }

        return cipher;
    }

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
            cipher[j] = algorithmOperations.stringToBigInt(s);
            cipher[j] = cipher[j].multiply(h.modPow(r,N)).mod(N);//C2
            cipher[j+1] = g.modPow(r,N);//C1
        }
        return cipher;
    }

    public String encryptFromStringToString(String message)
    {
        String str = new String();
        BigInteger[] bi_table = encrypt(message);
        for(int i = 0; i < bi_table.length; i++)
            str += bi_table[i] + "\n";
        return str;
    }

    public String decrypt(BigInteger[] cipher)
    {
        String s = new String();
        for (int i = 0; i < cipher.length; i+=2)
        { s += algorithmOperations.bigIntToString(cipher[i].multiply(cipher[i+1].modPow(a, N).modInverse(N)).mod(N));
        }
        return s;
    }

    public BigInteger[] decryptToBigInt(BigInteger[] cipher)
    {
        int len=(int)cipher.length/2;
        BigInteger[] wynik = new BigInteger[len];
        for (int i = 0, j=0; i < len; i++,j+=2)
            wynik[i] = cipher[j].multiply(cipher[j+1].modPow(a, N).modInverse(N)).mod(N);
        return wynik;
    }

    public String decryptFromStringToString(String cipher)
    {
        String[] wiersze = cipher.split("\n");
        BigInteger[] bi_table = new BigInteger[wiersze.length];
        for(int i = 0; i < wiersze.length; i++)
            bi_table[i] = new BigInteger(wiersze[i]);
        return decrypt(bi_table);
    }

}
