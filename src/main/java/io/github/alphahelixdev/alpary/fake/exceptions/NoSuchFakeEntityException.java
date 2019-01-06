package io.github.alphahelixdev.alpary.fake.exceptions;

public class NoSuchFakeEntityException extends NullPointerException {

    public NoSuchFakeEntityException() {
        super("No such Fake Entity!");
    }

    public NoSuchFakeEntityException(String s) {
        super("No such Fake Entity! (" + s + ")s");
    }
}
