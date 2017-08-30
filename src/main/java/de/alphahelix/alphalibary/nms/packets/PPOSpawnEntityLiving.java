package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOSpawnEntityLiving implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutSpawnEntityLiving",
                    ReflectionUtil.getNmsClass("EntityLiving"));

    private Object entity;

    public PPOSpawnEntityLiving(Object entity) {
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOSpawnEntityLiving setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity);
    }
}
