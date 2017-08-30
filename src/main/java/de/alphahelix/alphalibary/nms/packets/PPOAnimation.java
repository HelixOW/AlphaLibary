package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOAnimation implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutAnimation",
                    ReflectionUtil.getNmsClass("Entity"), int.class);

    private Object entity;
    private int animationType;

    public PPOAnimation(Object entity, int animationType) {
        this.entity = entity;
        this.animationType = animationType;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOAnimation setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public int getAnimationType() {
        return animationType;
    }

    public PPOAnimation setAnimationType(int animationType) {
        this.animationType = animationType;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity, animationType);
    }
}
