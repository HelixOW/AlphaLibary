package de.alphahelix.alphalibary.command.arguments;

import java.util.ArrayList;

public class BooleanArgument implements IArgument<Boolean> {

    private ArrayList<String> trueStrings = new ArrayList<>();
    private ArrayList<String> falseStrings = new ArrayList<>();

    public BooleanArgument() {
        addBooleans("true", "false");
        addBooleans("yes", "no");
        addBooleans("1", "0");
        addBooleans("y", "n");
    }

    @Override
    public boolean isCorrect(String arg) {
        return trueStrings.contains(arg.toLowerCase()) || falseStrings.contains(arg.toLowerCase());
    }

    @Override
    public Boolean get(String arg) {
        return isCorrect(arg) && trueStrings.contains(arg);
    }

    public void addBooleans(String trueString, String falseString) {
        if (!trueStrings.contains(trueString.toLowerCase())) {
            trueStrings.add(trueString.toLowerCase());
        }
        if (!falseStrings.contains(falseString.toLowerCase())) {
            falseStrings.add(falseString.toLowerCase());
        }
    }
}
