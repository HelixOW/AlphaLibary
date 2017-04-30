package de.alphahelix.alphalibary.entity;

public enum EntityAge {

    CHILD(0),
    ADULT(1);

    private int bukkitAge;

    EntityAge(int bukkitAge) {
        this.bukkitAge = bukkitAge;
    }

    public int getBukkitAge() {
        return bukkitAge;
    }
}
