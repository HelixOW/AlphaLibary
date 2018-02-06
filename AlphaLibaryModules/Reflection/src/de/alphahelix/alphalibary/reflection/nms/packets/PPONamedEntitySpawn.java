package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPONamedEntitySpawn that = (PPONamedEntitySpawn) o;
        return Objects.equal(getEntityHuman(), that.getEntityHuman());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntityHuman());
    }

    @Override
    public String toString() {
        return "PPONamedEntitySpawn{" +
                "entityHuman=" + entityHuman +
                '}';
    }
}
