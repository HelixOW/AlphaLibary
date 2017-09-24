package de.alphahelix.alphalibary.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.Util;

public class PPORelEntityMove implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntity$PacketPlayOutRelEntityMove",
                    int.class, long.class, long.class, long.class, boolean.class);

    private int entityID;
    private double x, y, z;
    private boolean onGround;

    public PPORelEntityMove(int entityID, double x, double y, double z, boolean onGround) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    public int getEntityID() {
        return entityID;
    }

    public PPORelEntityMove setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public double getX() {
        return x;
    }

    public PPORelEntityMove setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public PPORelEntityMove setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public PPORelEntityMove setZ(double z) {
        this.z = z;
        return this;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public PPORelEntityMove setOnGround(boolean onGround) {
        this.onGround = onGround;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entityID, Util.toDelta(x), Util.toDelta(y), Util.toDelta(z), onGround);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPORelEntityMove that = (PPORelEntityMove) o;
        return getEntityID() == that.getEntityID() &&
                Double.compare(that.getX(), getX()) == 0 &&
                Double.compare(that.getY(), getY()) == 0 &&
                Double.compare(that.getZ(), getZ()) == 0 &&
                isOnGround() == that.isOnGround();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntityID(), getX(), getY(), getZ(), isOnGround());
    }

    @Override
    public String toString() {
        return "PPORelEntityMove{" +
                "entityID=" + entityID +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", onGround=" + onGround +
                '}';
    }
}
