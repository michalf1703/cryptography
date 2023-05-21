package com.example.kryptozad2;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HexFormat;


public class ElGamal {
    private BigInteger p, k, h, g, c1, c2, Nm1;
    private String pKey = "";
    private String gKey = "";
    private String hKey = "";
    private String privateKey = "";
    private MillerRabin millerRabin = new MillerRabin();

    public void setP(BigInteger p) {
        this.p = p;
    }

    public void setH(BigInteger h) {
        this.h = h;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public void setC1(BigInteger c1) {
        this.c1 = c1;
    }

    public void setC2(BigInteger c2) {
        this.c2 = c2;
    }

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

    private BigInteger generateProbablePrime(int bitLength) {
        BigInteger prime;
        do {
            prime = new BigInteger(bitLength, new SecureRandom());
        } while (!millerRabin.isProbablePrime(prime));
        return prime;
    }

    private BigInteger generateRandomBigInteger(int bitLength) {
        return new BigInteger(bitLength, new SecureRandom());
    }

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

    public boolean verifyKeys() {
        return h.equals(g.modPow(k, p));
    }

    public byte[] decryptMessage() {
        BigInteger temp = c1.modPow(k, p);
        BigInteger temp2 = c2.divide(temp);
        return temp2.toByteArray();
    }

    public String getpKey() {
        return pKey;
    }

    public String getgKey() {
        return gKey;
    }

    public String gethKey() {
        return hKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.k = privateKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
