package de.alphahelix.alphalibary.nms;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public enum REnumHand {

    MAIN_HAND(0),
    OFF_HAND(1);

    private int nms;

    REnumHand(int nms) {
        this.nms = nms;
    }

    public Object getEnumHand() {
        return ReflectionUtil.getNmsClass("EnumHand").getEnumConstants()[nms];
    }
}
