package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utils.Util;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;


public class PPOEntityHeadRotation implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityHeadRotation",
                    ReflectionUtil.getNmsClass("Entity"), byte.class);

    private Object entity;
    private float yaw;

    public PPOEntityHeadRotation(Object entity, float yaw) {
        this.entity = entity;
        this.yaw = yaw;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOEntityHeadRotation setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public PPOEntityHeadRotation setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity, Util.toAngle(yaw));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOEntityHeadRotation that = (PPOEntityHeadRotation) o;
        return Float.compare(that.getYaw(), getYaw()) == 0 &&
                Objects.equal(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntity(), getYaw());
    }

    @Override
    public String toString() {
        return "PPOEntityHeadRotation{" +
                "entity=" + entity +
                ", yaw=" + yaw +
                '}';
    }
}
