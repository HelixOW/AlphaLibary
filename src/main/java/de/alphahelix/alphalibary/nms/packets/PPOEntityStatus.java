package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOEntityStatus implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityStatus",
                    ReflectionUtil.getNmsClass("Entity"), byte.class);

    private Object entity;
    private byte status;

    public PPOEntityStatus(Object entity, byte status) {
        this.entity = entity;
        this.status = status;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOEntityStatus setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public byte getStatus() {
        return status;
    }

    public PPOEntityStatus setStatus(byte status) {
        this.status = status;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity, status);
    }
}
