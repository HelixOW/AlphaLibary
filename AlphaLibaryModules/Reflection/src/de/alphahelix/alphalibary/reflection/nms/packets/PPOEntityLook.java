package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utils.MathUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;


public class PPOEntityLook implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntity$PacketPlayOutEntityLook",
                    int.class, byte.class, byte.class, boolean.class);

    private int entityID;
    private float yaw, pitch;
    private boolean onGround;

    public PPOEntityLook(int entityID, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public int getEntityID() {
        return entityID;
    }

    public PPOEntityLook setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public PPOEntityLook setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public PPOEntityLook setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public PPOEntityLook setOnGround(boolean onGround) {
        this.onGround = onGround;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entityID, MathUtil.toAngle(yaw), MathUtil.toAngle(pitch), onGround);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOEntityLook that = (PPOEntityLook) o;
        return getEntityID() == that.getEntityID() &&
                Float.compare(that.getYaw(), getYaw()) == 0 &&
                Float.compare(that.getPitch(), getPitch()) == 0 &&
                isOnGround() == that.isOnGround();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntityID(), getYaw(), getPitch(), isOnGround());
    }

    @Override
    public String toString() {
        return "PPOEntityLook{" +
                "entityID=" + entityID +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", onGround=" + onGround +
                '}';
    }
}
