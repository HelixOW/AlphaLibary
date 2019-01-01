package io.github.alphahelixdev.alpary.annotations.randoms;

import io.github.alphahelixdev.alpary.Alpary;

public class SimpleRandomListener {
    public SimpleRandomListener() {
        Alpary.getInstance().randomizeFields(this);
    }
}
