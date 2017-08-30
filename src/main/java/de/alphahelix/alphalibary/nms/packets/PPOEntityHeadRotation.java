package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOEntityHeadRotation implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityHeadRotation",
                    ReflectionUtil.getNmsClass("Entity"), byte.class);

    private Object entity;
    private float yaw;

    public PPOEntityHeadRotation(Object entity, float yaw) {
        this.entity = entity;
        this.yaw = yaw;
    }

    public Object getEntity() {
        return entity;
    }

    public PPOEntityHeadRotation setEntity(Object entity) {
        this.entity = entity;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public PPOEntityHeadRotation setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entity, FakeAPI.toAngle(yaw));
    }
}
