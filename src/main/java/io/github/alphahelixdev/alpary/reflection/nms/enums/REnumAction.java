package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;

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
        return Utils.nms().getNMSEnumConstant("PacketPlayInUseEntity$EnumEntityUseAction", c);
    }

    @Override
    public String toString() {
        return "REnumAction{" +
                "c=" + this.c +
                '}';
    }
}
