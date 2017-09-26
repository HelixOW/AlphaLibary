package de.alphahelix.alphalibary.command.arguments;

public class StringArgument extends Argument<String> {
    @Override
    public boolean matches() {
        return true;
    }

    @Override
    public String fromArgument() {
        return getEnteredArgument();
    }
}
