package de.alphahelix.alphalibary.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.util.Arrays;

public class PPOEntityDestroy implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityDestroy",
                    int[].class);

    private int[] entityIDs;

    public PPOEntityDestroy(int... entityIDs) {
        this.entityIDs = entityIDs;
    }

    public int[] getEntityIDs() {
        return entityIDs;
    }

    public PPOEntityDestroy setEntityIDs(int... entityIDs) {
        this.entityIDs = entityIDs;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entityIDs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOEntityDestroy that = (PPOEntityDestroy) o;
        return Objects.equal(getEntityIDs(), that.getEntityIDs());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntityIDs());
    }

    @Override
    public String toString() {
        return "PPOEntityDestroy{" +
                "entityIDs=" + Arrays.toString(entityIDs) +
                '}';
    }
}
