package de.alphahelix.alphalibary.reflection.nms.nbt;

import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NBTTileEntity that = (NBTTileEntity) o;
        return Objects.equal(tile, that.tile);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), tile);
    }

    @Override
    public String toString() {
        return "NBTTileEntity{" +
                "tile=" + tile +
                '}';
    }
}
