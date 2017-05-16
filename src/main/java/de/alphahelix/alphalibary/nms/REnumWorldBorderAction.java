package de.alphahelix.alphalibary.nms;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.io.Serializable;

public enum REnumWorldBorderAction implements Serializable {

    SET_SIZE(0),
    LERP_SIZE(1),
    SET_CENTER(2),
    INITIALIZE(3),
    SET_WARNING_TIME(4),
    SET_WARNING_BLOCKS(5);

    private final int index;

    REnumWorldBorderAction(int index) {
        this.index = index;
    }

    public Object getEnumWorldBorderAction() {
        return ReflectionUtil.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction").getEnumConstants()[index];
    }

    @Override
    public String toString() {
        return "REnumWorldBorderAction{" +
                "index=" + index +
                '}';
    }
}
