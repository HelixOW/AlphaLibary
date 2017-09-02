package de.alphahelix.alphalibary.statistics;

public class GameStatistic<T> {

    private String name;
    private T value;

    public GameStatistic(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
