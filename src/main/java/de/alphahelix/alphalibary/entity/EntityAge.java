package de.alphahelix.alphalibary.entity;

import java.io.Serializable;

public enum EntityAge implements Serializable {

    CHILD(0),
    ADULT(1);

    private int bukkitAge;

    EntityAge(int bukkitAge) {
        this.bukkitAge = bukkitAge;
    }

    public int getBukkitAge() {
        return bukkitAge;
    }

    @Override
    public String toString() {
        return "EntityAge{" +
                "bukkitAge=" + bukkitAge +
                '}';
    }
}
