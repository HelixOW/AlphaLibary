package de.alphahelix.alphalibary.command.arguments;

public class IntArgument extends Argument<Integer> {
    @Override
    public boolean matches() {
        try {
            Integer.parseInt(getEnteredArgument());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Integer fromArgument() {
        if (matches())
            return Integer.parseInt(getEnteredArgument());
        return 0;
    }
}
