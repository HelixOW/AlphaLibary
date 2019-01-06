package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class MountPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutMount"), Utils.nms().getNMSClass("Entity"));

    private Object entity;

    public MountPacket(Object entity) {
        this.entity = entity;
    }

    public static SaveConstructor getPacket() {
        return MountPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return MountPacket.getPacket().newInstance(stackTrace, this.getEntity());
    }

    public Object getEntity() {
        return this.entity;
    }

    public MountPacket setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MountPacket mountPacket = (MountPacket) o;
        return Objects.equals(this.getEntity(), mountPacket.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntity());
    }

    @Override
    public String toString() {
        return "MountPacket{" +
                "                            entity=" + this.entity +
                '}';
    }
}
