package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.utils.MathUtil;

import java.util.Objects;

public class EntityHeadRotationPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityHeadRotation"), Utils.nms().getNMSClass("Entity"),
            byte.class);

    private Object entity;
    private float yaw;

    public EntityHeadRotationPacket(Object entity, float yaw) {
        this.entity = entity;
        this.yaw = yaw;
    }

    public static SaveConstructor getPacket() {
        return EntityHeadRotationPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityHeadRotationPacket.getPacket().newInstance(stackTrace, this.getEntity(),
                MathUtil.toAngle(this.getYaw()));
    }

    public Object getEntity() {
        return this.entity;
    }

    public EntityHeadRotationPacket setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public float getYaw() {
        return this.yaw;
    }

    public EntityHeadRotationPacket setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityHeadRotationPacket that = (EntityHeadRotationPacket) o;
        return Float.compare(that.getYaw(), this.getYaw()) == 0 &&
                Objects.equals(this.getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntity(), this.getYaw());
    }

    @Override
    public String toString() {
        return "EntityHeadRotationPacket{" +
                "                            entity=" + this.entity +
                ",                             yaw=" + this.yaw +
                '}';
    }
}
