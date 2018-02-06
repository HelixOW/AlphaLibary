package de.alphahelix.alphalibary.command.arguments;

import com.google.common.base.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument<?> argument = (Argument<?>) o;
        return Objects.equal(getEnteredArgument(), argument.getEnteredArgument());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEnteredArgument());
    }

    @Override
    public String toString() {
        return "Argument{" +
                "enteredArgument='" + enteredArgument + '\'' +
                '}';
    }
}
