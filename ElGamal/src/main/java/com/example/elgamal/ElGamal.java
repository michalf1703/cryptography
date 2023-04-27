package com.example.elgamal;
import java.lang.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.security.*;

public class ElGamal {

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
}
