package com.example.kryptozad2;

import java.math.BigInteger;
import java.security.SecureRandom;

// Klasa implementująca algorytm Miller-Rabin do testowania pierwszości liczby
public class MillerRabin {
    private static final int DEFAULT_CERTAINTY = 40;

    // Sprawdzenie, czy liczba jest prawdopodobnie pierwsza
    public static boolean isProbablePrime(BigInteger n) {
        // Sprawdzenie podstawowych warunków brzegowych
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }

        if (n.compareTo(BigInteger.valueOf(3)) <= 0) {
            return true;
        }

        // Obliczenie parametrów s i d
        int s = 0;
        BigInteger d = n.subtract(BigInteger.ONE);
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            s++;
            d = d.divide(BigInteger.TWO);
        }

        int iterations = DEFAULT_CERTAINTY;
        for (int i = 0; i < iterations; i++) {
            // Wygenerowanie losowej podstawy
            BigInteger a = getRandomBase(n);

            // Obliczenie x = a^d mod n
            BigInteger x = a.modPow(d, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }

            boolean isComposite = true;
            for (int r = 1; r < s; r++) {
                // Obliczenie x = x^2 mod n
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(BigInteger.ONE)) {
                    return false;
                }
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    isComposite = false;
                    break;
                }
            }

            if (isComposite) {
                return false;
            }
        }

        return true;
    }

    // Generowanie losowej podstawy w zakresie od 2 do n-1
    private static BigInteger getRandomBase(BigInteger n) {
        BigInteger base;
        do {
            base = new BigInteger(n.bitLength(), new SecureRandom());
        } while (base.compareTo(BigInteger.ONE) <= 0 || base.compareTo(n) >= 0);
        return base;
    }
}
