package de.alphahelix.alphalibary.reflection.nms.nbt;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.inventory.ItemStack;


public class NBTItem extends NBTCompound {

    private ItemStack itemStack;

    public NBTItem(ItemStack itemStack) {
        super(null, null);
        this.itemStack = itemStack;
    }

    @Override
    public Object getCoumpound() {
        return ReflectionUtil.getItemRootNBTTagCompound(ReflectionUtil.getNMSItemStack(itemStack));
    }

    @Override
    public void setCompound(Object comp) {
        itemStack = ReflectionUtil.getBukkitItemStack(ReflectionUtil.setNBTTag(comp, ReflectionUtil.getNMSItemStack(itemStack)));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public NBTItem setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NBTItem nbtItem = (NBTItem) o;
        return Objects.equal(getItemStack(), nbtItem.getItemStack());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getItemStack());
    }

    @Override
    public String toString() {
        return "NBTItem{" +
                "itemStack=" + itemStack +
                '}';
    }
}
