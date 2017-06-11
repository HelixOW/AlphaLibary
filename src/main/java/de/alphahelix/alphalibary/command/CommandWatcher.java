package de.alphahelix.alphalibary.command;

import de.alphahelix.alphalibary.command.arguments.Argument;

import java.util.WeakHashMap;

public class CommandWatcher {

    private WeakHashMap<Integer, Argument<?>> args = new WeakHashMap<>();
    private String[] argsGiven;

    public CommandWatcher(String[] argsGiven) {
        this.argsGiven = argsGiven;
    }

    public CommandWatcher addArguments(Argument<?>... arguments) {
        for (Argument<?> a : arguments) {
            args.put(args.size(), a);
        }
        submit();
        return this;
    }

    private void submit() {
        if (this.args.size() == argsGiven.length) {
            for (int i : this.args.keySet()) {
                Argument<?> a = this.args.get(i);

                a.setEnteredArgument(argsGiven[i]);
            }
        }
    }

    public boolean isSameLenght() {
        return this.args.size() == argsGiven.length;
    }

    public boolean isSame() {
        if (!isSameLenght()) return false;

        for (Argument<?> givenArgument : this.args.values()) {
            if (!givenArgument.matches()) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T, A extends Argument<T>> Argument<T> getArgument(int index, Class<A> typus) {
        return ((Argument<T>) args.get(index));
    }
}
