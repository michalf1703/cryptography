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

    //zwraca podtablice z elementami od..do z podanej tablicy
    public static byte[] podtablica(byte dane[], int poczatek, int koniec)
    {
        byte[] subArray = new byte[koniec-poczatek];
        for(int i =0; poczatek < koniec; poczatek++, i++) subArray[i] = dane[poczatek];
        return subArray;
    }

    //konwertuje stringa na BigIntegera
    public static BigInteger stringToBigInt(String str)
    {
        byte[] tab = new byte[str.length()];
        for (int i = 0; i < tab.length; i++)
            tab[i] = (byte)str.charAt(i);
        return new BigInteger(1,tab);
    }

    //konwertuje BigIntegera na string
    public static String bigIntToString(BigInteger n)
    {
        byte[] tab = n.toByteArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tab.length; i++)
            sb.append((char)tab[i]);
        return sb.toString();
    }
    //konwertuje tablicę bajtów na ciąg znaków w systemie heksadecymalnym
    public static String bytesToHex(byte bytes[])
    {
        byte rawData[] = bytes;
        StringBuilder hexText = new StringBuilder();
        String initialHex = null;
        int initHexLength = 0;

        for (int i = 0; i < rawData.length; i++)
        {
            int positiveValue = rawData[i] & 0x000000FF;
            initialHex = Integer.toHexString(positiveValue);
            initHexLength = initialHex.length();
            while (initHexLength++ < 2)
            {
                hexText.append("0");
            }
            hexText.append(initialHex);
        }
        return hexText.toString();
    }

    public static byte[] hexToBytes(String tekst)
    {
        if (tekst == null) { return null;}
        else if (tekst.length() < 2) { return null;}
        else { if (tekst.length()%2!=0)tekst+='0';
            int dl = tekst.length() / 2;
            byte[] wynik = new byte[dl];
            for (int i = 0; i < dl; i++)
            { try{
                wynik[i] = (byte) Integer.parseInt(tekst.substring(i * 2, i * 2 + 2), 16);
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Problem z przekonwertowaniem HEX->BYTE.\n Sprawdź wprowadzone dane.", "Problem z przekonwertowaniem HEX->BYTE", JOptionPane.ERROR_MESSAGE); }
            }
            return wynik;
        }
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
                byte[] pom = podtablica(message, ileZnakow*i, ileZnakow*(i+1));
                cipher[j] = new BigInteger(1, pom);
                cipher[j] = cipher[j].multiply(h.modPow(r,N)).mod(N);//C2
                cipher[j+1] = g.modPow(r,N);//C1
            }
        }
        else
        {
            for (int i = 0, j=0; i < chunks-1; i++,j+=2)
            {
                byte[] pom = podtablica(message, ileZnakow*i, ileZnakow*(i+1));
                cipher[j] = new BigInteger(1, pom);
                cipher[j] = cipher[j].multiply(h.modPow(r,N)).mod(N);//C2
                cipher[j+1] = g.modPow(r,N);//C1
            }
            byte[] pom = podtablica(message, ileZnakow*(chunks-1), message.length);
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
            cipher[j] = stringToBigInt(s);
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
        { s += bigIntToString(cipher[i].multiply(cipher[i+1].modPow(a, N).modInverse(N)).mod(N));
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
