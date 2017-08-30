package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPONamedEntitySpawn implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutNamedEntitySpawn",
                    ReflectionUtil.getNmsClass("EntityHuman"));

    private Object entityHuman;

    public PPONamedEntitySpawn(Object entityHuman) {
        this.entityHuman = entityHuman;
    }

    public Object getEntityHuman() {
        return entityHuman;
    }

    public PPONamedEntitySpawn setEntityHuman(Object entityHuman) {
        this.entityHuman = entityHuman;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entityHuman);
    }
}
