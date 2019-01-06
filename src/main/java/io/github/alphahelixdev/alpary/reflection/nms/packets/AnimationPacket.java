package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.AnimationType;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

public class AnimationPacket implements IPacket {

    private static final SaveConstructor PACKET =
            NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutAnimation"),
                    Utils.nms().getNMSClass("Entity"), int.class);

    private Object entity;
    private int animationType;

    public AnimationPacket(Object entity, AnimationType type) {
        this(entity, type.ordinal());
    }

    public AnimationPacket(Object entity, int animationType) {
        this.setEntity(entity);
        this.animationType = animationType;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return getPacket().newInstance(stackTrace, this.getEntity(), this.getAnimationType());
    }

    private SaveConstructor getPacket() {
        return PACKET;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public int getAnimationType() {
        return animationType;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }
}
