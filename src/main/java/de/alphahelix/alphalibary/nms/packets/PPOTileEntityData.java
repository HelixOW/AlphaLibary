package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.nbt.NBTCompound;
import de.alphahelix.alphalibary.nms.BlockPos;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

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
}
