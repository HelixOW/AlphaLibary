package de.alphahelix.alphalibary.command.arguments;

public abstract class Argument<T> {

    private String enteredArgument;

    public String getEnteredArgument() {
        return enteredArgument;
    }

    public Argument setEnteredArgument(String enteredArgument) {
        this.enteredArgument = enteredArgument;
        return this;
    }

    public abstract boolean matches();

    public abstract T fromArgument();

}
