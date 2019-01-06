package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class SpawnEntityLivingPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutSpawnEntityLiving"),
            Utils.nms().getNMSClass("EntityLiving"));

    private Object entity;

    public SpawnEntityLivingPacket(Object entity) {
        this.entity = entity;
    }

    private static SaveConstructor getPacket() {
        return SpawnEntityLivingPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return SpawnEntityLivingPacket.getPacket().newInstance(stackTrace, this.getEntity());
    }

    public Object getEntity() {
        return this.entity;
    }

    public SpawnEntityLivingPacket setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpawnEntityLivingPacket that = (SpawnEntityLivingPacket) o;
        return Objects.equals(this.getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntity());
    }

    @Override
    public String toString() {
        return "SpawnEntityLivingPacket{" +
                "                            entity=" + this.entity +
                '}';
    }
}
