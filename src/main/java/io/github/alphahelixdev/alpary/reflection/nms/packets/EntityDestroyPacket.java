package io.github.alphahelixdev.alpary.reflection.nms.packets;

import com.google.common.base.Objects;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Arrays;

public class EntityDestroyPacket implements IPacket {

    private static final SaveConstructor PACKET =
            NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutEntityDestroy"),
                    int[].class);

    private int[] entityIDs;

    public EntityDestroyPacket(int... entityIDs) {
        this.setEntityIDs(entityIDs);
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityDestroyPacket.PACKET.newInstance(stackTrace, this.getEntityIDs());
    }

    public int[] getEntityIDs() {
        return this.entityIDs;
    }

    public EntityDestroyPacket setEntityIDs(int... entityIDs) {
        this.entityIDs = entityIDs;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode((Object) getEntityIDs());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDestroyPacket that = (EntityDestroyPacket) o;
        return Objects.equal(getEntityIDs(), that.getEntityIDs());
    }

    @Override
    public String toString() {
        return "EntityDestroyPacket{" +
                "entityIDs=" + Arrays.toString(entityIDs) +
                '}';
    }
}
