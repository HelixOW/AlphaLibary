package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;

import java.io.Serializable;

public enum REnumHand implements Serializable {

    MAIN_HAND(0),
    OFF_HAND(1);

    private final int nms;

    REnumHand(int nms) {
        this.nms = nms;
    }

    public Object getEnumHand() {
        return Utils.nms().getNMSEnumConstant("EnumHand", nms);
    }

    @Override
    public String toString() {
        return "REnumHand{" +
                "nms=" + this.nms +
                '}';
    }
}
