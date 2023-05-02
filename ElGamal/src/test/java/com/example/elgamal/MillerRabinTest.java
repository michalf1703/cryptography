package com.example.elgamal;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class MillerRabinTest {

    @Test
    void isPrime() {
        // Test for prime number
        BigInteger primeNumber = BigInteger.valueOf(65537);
        assertTrue(MillerRabin.isPrime(primeNumber), "Failed for prime number");

        // Test for composite number
        BigInteger compositeNumber = BigInteger.valueOf(1001);
        assertFalse(MillerRabin.isPrime(compositeNumber), "Failed for composite number");

        // Test for boundary cases
        BigInteger one = BigInteger.ONE;
        BigInteger two = BigInteger.TWO;
        BigInteger three = BigInteger.valueOf(3);

        assertTrue(MillerRabin.isPrime(two), "Failed for 2");
        assertTrue(MillerRabin.isPrime(three), "Failed for 3");
        assertFalse(MillerRabin.isPrime(one), "Failed for 1");
    }
}