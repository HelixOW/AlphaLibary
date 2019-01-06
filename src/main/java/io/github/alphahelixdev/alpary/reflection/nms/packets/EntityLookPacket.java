package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.utils.MathUtil;

import java.util.Objects;

public class EntityLookPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntity$PacketPlayOutEntityLook"), int.class, byte.class,
            byte.class, boolean.class);

    private int entityID;
    private float yaw, pitch;
    private boolean onGround;

    public EntityLookPacket(int entityID, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    private static SaveConstructor getPacket() {
        return EntityLookPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityLookPacket.getPacket().newInstance(stackTrace, this.getEntityID(), MathUtil.toAngle(this.getYaw()),
                MathUtil.toAngle(this.getPitch()), this.isOnGround());
    }

    public int getEntityID() {
        return this.entityID;
    }

    public EntityLookPacket setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public float getYaw() {
        return this.yaw;
    }

    public EntityLookPacket setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return this.pitch;
    }

    public EntityLookPacket setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public EntityLookPacket setOnGround(boolean onGround) {
        this.onGround = onGround;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityLookPacket that = (EntityLookPacket) o;
        return this.getEntityID() == that.getEntityID() &&
                Float.compare(that.getYaw(), getYaw()) == 0 &&
                Float.compare(that.getPitch(), getPitch()) == 0 &&
                this.isOnGround() == that.isOnGround();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntityID(), this.getYaw(), this.getPitch(), this.isOnGround());
    }

    @Override
    public String toString() {
        return "EntityLookPacket{" +
                "                            entityID=" + this.entityID +
                ",                             yaw=" + this.yaw +
                ",                             pitch=" + this.pitch +
                ",                             onGround=" + this.onGround +
                '}';
    }
}
