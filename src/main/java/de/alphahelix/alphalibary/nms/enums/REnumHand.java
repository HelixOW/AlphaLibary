package de.alphahelix.alphalibary.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.io.Serializable;

public enum REnumHand implements Serializable {

    MAIN_HAND(0),
    OFF_HAND(1);

    private int nms;

    REnumHand(int nms) {
        this.nms = nms;
    }

    public Object getEnumHand() {
        return ReflectionUtil.getNmsClass("EnumHand").getEnumConstants()[nms];
    }

    @Override
    public String toString() {
        return "REnumHand{" +
                "nms=" + nms +
                '}';
    }
}
