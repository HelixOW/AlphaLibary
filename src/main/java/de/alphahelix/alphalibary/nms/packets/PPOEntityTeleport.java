package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOEntityTeleport implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityTeleport",
                    ReflectionUtil.getNmsClass("Entity"));

    private Object entity;

    public PPOEntityTeleport(Object entity) {
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOEntityTeleport setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity);
    }
}
