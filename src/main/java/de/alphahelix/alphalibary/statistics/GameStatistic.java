package de.alphahelix.alphalibary.statistics;

import com.google.common.base.Objects;

public class GameStatistic<T> {

    private final String name;
    private final T value;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatistic<?> that = (GameStatistic<?>) o;
        return Objects.equal(getName(), that.getName()) &&
                Objects.equal(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getValue());
    }

    @Override
    public String toString() {
        return "GameStatistic{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
