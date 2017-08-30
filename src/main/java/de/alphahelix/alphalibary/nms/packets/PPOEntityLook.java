package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class PPOEntityLook implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntity$PacketPlayOutEntityLook",
                    int.class, byte.class, byte.class, boolean.class);

    private int entityID;
    private float yaw, pitch;
    private boolean onGround;

    public PPOEntityLook(int entityID, float yaw, float pitch, boolean onGround) {
        this.entityID = entityID;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public int getEntityID() {
        return entityID;
    }

    public PPOEntityLook setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public PPOEntityLook setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public PPOEntityLook setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public PPOEntityLook setOnGround(boolean onGround) {
        this.onGround = onGround;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entityID, FakeAPI.toAngle(yaw), FakeAPI.toAngle(pitch), onGround);
    }
}
