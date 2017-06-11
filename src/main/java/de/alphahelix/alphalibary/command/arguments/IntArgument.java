package de.alphahelix.alphalibary.command.arguments;

public class IntArgument implements IArgument<Integer> {
    @Override
    public boolean isCorrect(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Integer get(String arg) {
        if (isCorrect(arg))
            return Integer.parseInt(arg);
        else
            return 0;
    }
}
