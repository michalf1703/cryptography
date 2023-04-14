package com.example.des3;

import java.lang.*;
public class TripleDES extends DES
{   String s_key1, s_key2, s_key3;



    public byte[] encode3DES(byte[] message) throws DESKeyException
    {
        int runda = 1;
        setKeyHex(s_key1);
        byte[] result = kodowanie(message,runda);
        setKeyHex(s_key2);
        runda = 2;
        result = dekodowanie(result,runda);
        setKeyHex(s_key3);
        runda = 3;
        result = kodowanie(result,runda);
        return result;
    }


    public byte[] decode3DES (byte[] cipher)throws DESKeyException
    {
        int runda = 1;
        setKeyHex(s_key3);
        byte[]result = dekodowanie(cipher,runda);
        runda = 2;
        setKeyHex(s_key2);
        result = kodowanie(result, runda);
        setKeyHex(s_key1);
        runda = 3;
        result = dekodowanie(result, runda);
        return result;
    }


}
