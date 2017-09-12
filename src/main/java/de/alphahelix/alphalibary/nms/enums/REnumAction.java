package de.alphahelix.alphalibary.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.io.Serializable;

public enum REnumAction implements Serializable {

    INTERACT(0),
    ATTACK(1),
    INTERACT_AT(2);

    private final int c;

    REnumAction(int c) {
        this.c = c;
    }

    public Object getEnumAction() {
        return ReflectionUtil.getNmsClass("PacketPlayInUseEntity$EnumEntityUseAction").getEnumConstants()[c];
    }

    @Override
    public String toString() {
        return "REnumAction{" +
                "c=" + c +
                '}';
    }
}
