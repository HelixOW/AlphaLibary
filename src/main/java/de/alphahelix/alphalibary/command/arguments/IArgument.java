package de.alphahelix.alphalibary.command.arguments;

public interface IArgument<T> {

    boolean isCorrect(String arg);

    T get(String arg);
}
