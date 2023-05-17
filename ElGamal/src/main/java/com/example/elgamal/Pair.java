package com.example.elgamal;

import java.io.Serializable;

// Zajebane ze Stacka
public class Pair<T, U> implements Serializable {
    public final T first;
    public final U second;

    public Pair(T t, U u) {
        this.first = t;
        this.second = u;
    }
}
