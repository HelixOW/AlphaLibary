package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;

public enum REnumDifficulty {

    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3);

    private final int id;

    REnumDifficulty(int id) {
        this.id = id;
    }

    public Object getEnumDifficulty() {
        return Utils.nms().getNMSEnumConstant("EnumDifficulty", id);
    }


    @Override
    public String toString() {
        return "REnumDifficulty{" +
                "                            id=" + this.id +
                '}';
    }
}
