package com.example.elgamal;

import javax.swing.*;
import java.math.BigInteger;

public class AlgorithmOperations {
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
}
