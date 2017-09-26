package de.alphahelix.alphalibary.core.utils.entity;

import java.io.Serializable;

public enum EntityAge implements Serializable {

    CHILD(0),
    ADULT(1);

    private final int bukkitAge;

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
