package com.example.kryptozad2;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HexFormat;

// Klasa implementująca algorytm ElGamal do szyfrowania i deszyfrowania wiadomości
public class ElGamal {
    private BigInteger p, k, h, g, c1, c2, Nm1;
    private String pKey = "";
    private String gKey = "";
    private String hKey = "";
    private String privateKey = "";
    private MillerRabin millerRabin = new MillerRabin();

    // Ustawienie wartości p
    public void setP(BigInteger p) {
        this.p = p;
    }

    // Ustawienie wartości h
    public void setH(BigInteger h) {
        this.h = h;
    }

    // Ustawienie wartości g
    public void setG(BigInteger g) {
        this.g = g;
    }

    // Ustawienie wartości c1
    public void setC1(BigInteger c1) {
        this.c1 = c1;
    }

    // Ustawienie wartości c2
    public void setC2(BigInteger c2) {
        this.c2 = c2;
    }

    // Generowanie kluczy
    public void generateKeys() {
        p = generateProbablePrime(512);
        g = generateRandomBigInteger(510);
        k = generateRandomBigInteger(510);
        h = g.modPow(k, p);
        Nm1 = p.subtract(BigInteger.ONE);

        HexFormat hex = HexFormat.of();
        pKey = hex.formatHex(p.toByteArray());
        gKey = hex.formatHex(g.toByteArray());
        hKey = hex.formatHex(h.toByteArray());
        privateKey = hex.formatHex(k.toByteArray());
    }

    // Generowanie prawdopodobnej liczby pierwszej o określonej długości bitowej
    private BigInteger generateProbablePrime(int bitLength) {
        BigInteger prime;
        do {
            prime = new BigInteger(bitLength, new SecureRandom());
        } while (!millerRabin.isProbablePrime(prime));
        return prime;
    }

    // Generowanie losowej liczby BigInteger o określonej długości bitowej
    private BigInteger generateRandomBigInteger(int bitLength) {
        return new BigInteger(bitLength, new SecureRandom());
    }

    // Szyfrowanie wiadomości
    public String[] encryptMessage(byte[] toEncrypt) {
        BigInteger b = generateRandomBigInteger(500);
        if (Nm1 == null) {
            Nm1 = p.subtract(BigInteger.ONE);
        }

        BigInteger temp = new BigInteger(toEncrypt);
        BigInteger c1 = g.modPow(b, p);
        BigInteger c2 = h.modPow(b, p);
        c2 = c2.multiply(temp);
        String[] parts = new String[2];
        parts[0] = c1.toString();
        parts[1] = c2.toString();
        return parts;
    }

    // Weryfikacja kluczy
    public boolean verifyKeys() {
        return h.equals(g.modPow(k, p));
    }

    // Deszyfrowanie wiadomości
    public byte[] decryptMessage() {
        BigInteger temp = c1.modPow(k, p);
        BigInteger temp2 = c2.divide(temp);
        return temp2.toByteArray();
    }

    // Pobranie klucza publicznego p
    public String getpKey() {
        return pKey;
    }

    // Pobranie klucza publicznego g
    public String getgKey() {
        return gKey;
    }

    // Pobranie klucza publicznego h
    public String gethKey() {
        return hKey;
    }

    // Ustawienie klucza prywatnego
    public void setPrivateKey(BigInteger privateKey) {
        this.k = privateKey;
    }

    // Pobranie klucza prywatnego
    public String getPrivateKey() {
        return privateKey;
    }
}
