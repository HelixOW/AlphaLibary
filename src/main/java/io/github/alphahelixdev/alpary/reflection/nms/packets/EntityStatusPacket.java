package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.EntityStatus;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class EntityStatusPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityStatus"),
            Utils.nms().getNMSClass("Entity"), byte.class);

    private Object entity;
    private EntityStatus status;

    public EntityStatusPacket(Object entity, EntityStatus status) {
        this.entity = entity;
        this.status = status;
    }

    private static SaveConstructor getPacket() {
        return EntityStatusPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityStatusPacket.getPacket().newInstance(stackTrace, this.getEntity(), this.getStatus().getId());
    }

    public Object getEntity() {
        return this.entity;
    }

    public EntityStatusPacket setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public EntityStatus getStatus() {
        return this.status;
    }

    public EntityStatusPacket setStatus(EntityStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityStatusPacket that = (EntityStatusPacket) o;
        return Objects.equals(this.getEntity(), that.getEntity()) &&
                this.getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntity(), this.getStatus());
    }

    @Override
    public String toString() {
        return "EntityStatusPacket{" +
                "                            entity=" + this.entity +
                ",                             status=" + this.status +
                '}';
    }
}
