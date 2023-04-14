package com.example.des3;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitOperations {
    public BitOperations(){}

    // getBit umożliwia odczytanie pojedyńczego bitu z tablicy bajtów

    public static int getBit(byte[] data, int pos) {
        int posByte = pos / 8;
        int posBit = pos % 8;
        byte valByte = data[posByte];
        int valInt = valByte >> (7 - posBit) & 1;
        return valInt;
    }

    public static void setBit(byte[] data, int pos, int oneOrZero) {
        int posByte = pos / 8;
        int posBit = (7 - (pos % 8));
        if (oneOrZero == 1) {               // set 1
            data[posByte] |= (1 << posBit);
        }
        else {                              // set 0
            data[posByte] &= ~(1 << posBit);
        }
    }

    //xoruje dwie tablice bajtów
    public static byte[] xor(byte[] a, byte[] b)
    {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++)
        {
            out[i] = (byte) (a[i] ^ b[i]);
        }
        return out;
    }


    //cykliczne przesunięcie bitów w lewo o zadana ilość pozycji
    public static byte[] przesunWlewo(byte[] in, int len, int step)
    {
        byte[] out = new byte[(len - 1) / 8 + 1];
        for (int i = 0; i < len; i++)
        {
            int val = getBit(in, (i + step) % len);
            setBit(out, i, val);
        }
        return out;
    }

    //zwraca podaną ilość bitów od podanej pozycji z tablicy bajtów
    public static byte[] selectBits(byte[] in, int pos, int len)
    {
        int numOfBytes = (len - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < len; i++) {
            int val = getBit(in, pos + i);
            setBit(out, i, val);
        }
        return out;
    }

    //wybiera bity z tablicy bajtów i zwraca w nowej tablicy
    //wybierane te bity, które wskazane w drugim parametrze(każdy bajt tablicy map jest numerem bitu do pobrania z tablicy in)
    public static byte[] selectBits(byte[] in, byte[] map)
    {
        int numOfBytes = (map.length - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < map.length; i++)
        {
            int val = getBit(in, map[i] - 1);
            setBit(out, i, val);
        }
        return out;
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

    //konwertuje ciąg znaków w systemie heksadecymalnym na tablicę bajtów
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
