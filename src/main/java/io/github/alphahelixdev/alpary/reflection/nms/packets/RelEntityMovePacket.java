package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import io.github.alphahelixdev.helius.utils.MathUtil;

public class RelEntityMovePacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntity$PacketPlayOutRelEntityMove"), int.class, long.class,
            long.class, long.class, boolean.class);

    private int entityID;
    private double x, y, z;
    private boolean onGround;

    public RelEntityMovePacket(int entityID, double x, double y, double z, boolean onGround) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    private static SaveConstructor getPacket() {
        return RelEntityMovePacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return RelEntityMovePacket.getPacket().newInstance(stackTrace, this.getEntityID(),
                MathUtil.toDelta(this.getX()), MathUtil.toDelta(this.getY()), MathUtil.toDelta(this.getZ()),
                this.isOnGround());
    }

    public int getEntityID() {
        return this.entityID;
    }

    public RelEntityMovePacket setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public double getX() {
        return this.x;
    }

    public RelEntityMovePacket setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return this.y;
    }

    public RelEntityMovePacket setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return this.z;
    }

    public RelEntityMovePacket setZ(double z) {
        this.z = z;
        return this;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public RelEntityMovePacket setOnGround(boolean onGround) {
        this.onGround = onGround;
        return this;
    }
}
