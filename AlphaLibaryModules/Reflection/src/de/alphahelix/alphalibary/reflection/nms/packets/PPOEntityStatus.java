package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.EntityStatus;

@SuppressWarnings("ALL")
public class PPOEntityStatus implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityStatus",
                    ReflectionUtil.getNmsClass("Entity"), byte.class);

    private Object entity;
    private EntityStatus status;

    public PPOEntityStatus(Object entity, EntityStatus status) {
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

    public EntityStatus getStatus() {
        return status;
    }

    public PPOEntityStatus setStatus(EntityStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity, status.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOEntityStatus that = (PPOEntityStatus) o;
        return getStatus() == that.getStatus() &&
                Objects.equal(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntity(), getStatus());
    }

    @Override
    public String toString() {
        return "PPOEntityStatus{" +
                "entity=" + entity +
                ", status=" + status +
                '}';
    }
}
