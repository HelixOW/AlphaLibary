package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOMount implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutMount",
                    ReflectionUtil.getNmsClass("Entity"));

    private Object entity;

    public PPOMount(Object entity) {
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOMount setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity);
    }
}
