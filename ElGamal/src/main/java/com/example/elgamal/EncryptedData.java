package com.example.elgamal;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class EncryptedData implements Serializable {
    private ArrayList<Pair<BigInteger, Integer>> content = new ArrayList<>();

    private boolean isNegative = false;

    public void appendContent(int index, BigInteger value, Integer numberOfZeroes) {
        this.content.add(index, new Pair<>(value, numberOfZeroes));
    }

    public Pair<BigInteger, Integer> getContent(int index) {
        return content.get(index);
    }

    public int getSize() {
        return content.size();
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
    }
}
