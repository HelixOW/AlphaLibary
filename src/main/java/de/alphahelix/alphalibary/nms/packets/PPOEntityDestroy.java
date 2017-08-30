package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

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
}
