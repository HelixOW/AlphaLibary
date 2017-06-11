package de.alphahelix.alphalibary.command;

import de.alphahelix.alphalibary.command.arguments.IArgument;

import java.util.HashMap;

public class CommandWatcher {

    private HashMap<Integer, IArgument<?>> arguments = new HashMap<>();
    private String[] argumentsGiven = null;

    public CommandWatcher(String[] argumentsGiven) {
        this.argumentsGiven = argumentsGiven;
    }

    public CommandWatcher addArgument(IArgument<?> arg) {
        arguments.put(arguments.size(), arg);
        return this;
    }

    public CommandWatcher addArguments(IArgument<?>... args) {
        for (IArgument<?> arg : args) {
            addArgument(arg);
        }
        return this;
    }

    public boolean isComperable() {
        if (this.arguments.size() != argumentsGiven.length)
            return false;

        for (int i = 0; i < argumentsGiven.length; i++) {
            String currentArg = argumentsGiven[i];
            IArgument<?> givenIArgument = this.arguments.get(i);

            if (!givenIArgument.isCorrect(currentArg))
                return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(int index) {
        return ((IArgument<T>) arguments.get(index)).get(argumentsGiven[index]);
    }
}
