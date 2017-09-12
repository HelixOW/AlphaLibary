package de.alphahelix.alphalibary.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.nms.enums.AnimationType;
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

    public PPOAnimation(Object entity, AnimationType type) {
        this(entity, type.ordinal());
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

    public PPOAnimation setAnimationType(AnimationType animationType) {
        return this.setAnimationType(animationType.ordinal());
    }

    public PPOAnimation setAnimationType(int animationType) {
        this.animationType = animationType;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity, animationType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOAnimation that = (PPOAnimation) o;
        return getAnimationType() == that.getAnimationType() &&
                Objects.equal(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntity(), getAnimationType());
    }

    @Override
    public String toString() {
        return "PPOAnimation{" +
                "entity=" + entity +
                ", animationType=" + animationType +
                '}';
    }
}
