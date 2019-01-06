package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class NamedEntitySpawnPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutNamedEntitySpawn"),
            Utils.nms().getNMSClass("EntityHuman"));

    private Object entityHuman;

    public NamedEntitySpawnPacket(Object entityHuman) {
        this.entityHuman = entityHuman;
    }

    private static SaveConstructor getPacket() {
        return NamedEntitySpawnPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return NamedEntitySpawnPacket.getPacket().newInstance(stackTrace, this.getEntityHuman());
    }

    public Object getEntityHuman() {
        return this.entityHuman;
    }

    public NamedEntitySpawnPacket setEntityHuman(Object entityHuman) {
        this.entityHuman = entityHuman;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntitySpawnPacket that = (NamedEntitySpawnPacket) o;
        return Objects.equals(this.getEntityHuman(), that.getEntityHuman());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntityHuman());
    }

    @Override
    public String toString() {
        return "NamedEntitySpawnPacket{" +
                "                            entityHuman=" + this.entityHuman +
                '}';
    }
}
