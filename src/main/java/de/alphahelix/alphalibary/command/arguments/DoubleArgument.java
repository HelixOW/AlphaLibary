package de.alphahelix.alphalibary.command.arguments;

public class DoubleArgument implements IArgument<Double> {
    @Override
    public boolean isCorrect(String arg) {
        try {
            Double.parseDouble(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Double get(String arg) {
        if (isCorrect(arg))
            return Double.parseDouble(arg);
        else
            return 0.0;
    }
}
