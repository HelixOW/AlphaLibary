package io.github.alphahelixdev.alpary.game;

import java.util.Objects;
import java.util.UUID;

public class GameCurrency {

    private final UUID owner;
    private float amount;
    private boolean minus;

    public GameCurrency(UUID owner) {
        this(owner, 0, false);
    }

    public GameCurrency(UUID owner, float amount) {
        this(owner, amount, false);
    }

    public GameCurrency(UUID owner, float amount, boolean minus) {
        this.owner = owner;
        this.amount = amount;
        this.minus = minus;
    }

    public GameCurrency add(float amount) {
        this.amount += amount;
        return this;
    }

    public GameCurrency remove(float amount) {
        if (this.amount - amount < 0 && !minus)
            this.amount = 0.0f;
        else
            this.amount -= amount;
        return this;
    }

    public UUID getOwner() {
        return owner;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isMinus() {
        return minus;
    }

    public void setMinus(boolean minus) {
        this.minus = minus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCurrency that = (GameCurrency) o;
        return Float.compare(that.amount, amount) == 0 &&
                minus == that.minus &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, amount, minus);
    }

    @Override
    public String toString() {
        return "GameCurrency{" +
                "owner=" + owner +
                ", amount=" + amount +
                ", minus=" + minus +
                '}';
    }
}
