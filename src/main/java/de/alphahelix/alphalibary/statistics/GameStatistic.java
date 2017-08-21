package de.alphahelix.alphalibary.statistics;

public class GameStatistic {

    private String name;
    private Object value;

    public GameStatistic(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
