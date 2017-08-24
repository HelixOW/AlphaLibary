package de.alphahelix.alphalibary.economy;

import java.util.UUID;

public class Economy {

    private final UUID owner;
    private long money;
    private boolean minus = false;

    public Economy(UUID owner) {
        this(owner, 0, false);
    }

    public Economy(UUID owner, long money) {
        this(owner, money, false);
    }

    public Economy(UUID owner, long money, boolean minus) {
        this.owner = owner;
        this.money = money;
        this.minus = minus;
    }

    public UUID getOwner() {
        return owner;
    }

    public long getMoney() {
        return money;
    }

    public Economy setMoney(long money) {
        this.money = money;
        return this;
    }

    public boolean isMinus() {
        return minus;
    }

    public Economy addMoney(long money) {
        this.money += money;
        return this;
    }

    public Economy removeMoney(long money) {
        if (this.money - money < 0 && !isMinus())
            this.money = 0;
        else
            this.money -= money;
        return this;
    }
}
