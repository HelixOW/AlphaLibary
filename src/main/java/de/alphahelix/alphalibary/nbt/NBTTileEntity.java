package de.alphahelix.alphalibary.nbt;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.block.BlockState;

public class NBTTileEntity extends NBTCompound {

    private final BlockState tile;

    public NBTTileEntity(BlockState tile) {
        super(null, null);
        this.tile = tile;
    }

    @Override
    public Object getCoumpound() {
        return ReflectionUtil.getTileEntityNBTTagCompound(tile);
    }

    @Override
    public void setCompound(Object comp) {
        ReflectionUtil.setTileEntityNBTTagCompound(tile, comp);
    }
}
