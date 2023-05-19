package com.example.kryptozad2;

import java.math.BigInteger;
import java.util.Random;

public class MillerRabin {

    private static final int CERTAINTY = 10; // liczba iteracji testu

    public static boolean isPrime(BigInteger n) {

        // Sprawdzamy, czy liczba n jest większa niż 1 (liczba 1 nie jest liczba pierwsza)
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }
        // Przyjmujemy, że liczby 2 i 3 są pierwsze
        if (n.equals(BigInteger.valueOf(2)) || n.equals(BigInteger.valueOf(3))) {
            return true;
        }

        // Sprawdzamy, czy liczba jest parzysta - jeśli tak, to nie jest pierwsza
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }

        // n - 1 = d * 2^s, gdzie d jest nieparzyste
        BigInteger d = n.subtract(BigInteger.ONE);
        int s = 0;
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            s++;
        }

        // Wykonujemy test CERTAINTY razy
        for (int i = 0; i < CERTAINTY; i++) {
            // Losujemy liczbę a z zakresu [2, n - 2]
            BigInteger a = BigInteger.valueOf(2 + new Random().nextInt(n.intValue() - 3));

            // Obliczamy a^d mod n
            BigInteger x = a.modPow(d, n);

            // Jeśli x = 1 lub x = n - 1, to n może być pierwsza, kontynuujemy test
            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }

            // Obliczamy kolejne potęgi a^d mod n
            boolean isPrime = false;
            for (int j = 0; j < s - 1; j++) {
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(BigInteger.ONE)) {
                    return false; // n nie jest pierwsza
                }
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    isPrime = true;
                    break; // kontynuujemy test
                }
            }
            if (!isPrime) {
                return false; // n nie jest pierwsza
            }
        }

        return true; // n może być pierwsza
    }
}