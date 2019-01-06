package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class EntityTeleportPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityTeleport"), Utils.nms().getNMSClass("Entity"));

    private Object entity;

    public EntityTeleportPacket(Object entity) {
        this.entity = entity;
    }

    private static SaveConstructor getPacket() {
        return EntityTeleportPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityTeleportPacket.getPacket().newInstance(stackTrace, this.getEntity());
    }

    public Object getEntity() {
        return this.entity;
    }

    public EntityTeleportPacket setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityTeleportPacket that = (EntityTeleportPacket) o;
        return Objects.equals(this.getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntity());
    }

    @Override
    public String toString() {
        return "EntityTeleportPacket{" +
                "                            entity=" + this.entity +
                '}';
    }
}
