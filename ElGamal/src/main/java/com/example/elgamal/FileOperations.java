package com.example.elgamal;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;

public class FileOperations {
    public FileOperations() {}

    public static byte[] wczytajZpliku(String nazwa_pliku) throws Exception
    {
        FileInputStream fis = new FileInputStream(nazwa_pliku);
        int ileWPliku = fis.available();
        byte[] dane = new byte[ileWPliku];
        fis.read(dane);
        fis.close();
        return dane;
    }

    //zapisuje do pliku o podanej nazwie zawartość tablicy bajtów
    public static void zapiszDoPliku(byte dane[], String nazwa_pliku) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(nazwa_pliku);
        fos.write(dane);
        fos.close();
    }
}
