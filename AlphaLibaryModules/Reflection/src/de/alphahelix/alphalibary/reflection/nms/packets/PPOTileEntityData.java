package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.BlockPos;
import de.alphahelix.alphalibary.reflection.nms.nbt.NBTCompound;

@SuppressWarnings("ALL")
public class PPOTileEntityData implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutTileEntityData",
                    ReflectionUtil.getNmsClass("BlockPosition"), int.class, ReflectionUtil.getNmsClass("NBTTagCompound"));

    private BlockPos pos;
    private int action;
    private NBTCompound compound;

    public PPOTileEntityData(BlockPos pos, int action, NBTCompound compound) {
        this.pos = pos;
        this.action = action;
        this.compound = compound;
    }

    public BlockPos getPos() {
        return pos;
    }

    public PPOTileEntityData setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    public int getAction() {
        return action;
    }

    public PPOTileEntityData setAction(int action) {
        this.action = action;
        return this;
    }

    public NBTCompound getCompound() {
        return compound;
    }

    public PPOTileEntityData setCompound(NBTCompound compound) {
        this.compound = compound;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, ReflectionUtil.toBlockPosition(pos), action, compound.getCoumpound());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOTileEntityData that = (PPOTileEntityData) o;
        return getAction() == that.getAction() &&
                Objects.equal(getPos(), that.getPos()) &&
                Objects.equal(getCompound(), that.getCompound());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPos(), getAction(), getCompound());
    }

    @Override
    public String toString() {
        return "PPOTileEntityData{" +
                "pos=" + pos +
                ", action=" + action +
                ", compound=" + compound +
                '}';
    }
}
