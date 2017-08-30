package de.alphahelix.alphalibary.nbt;

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
}
