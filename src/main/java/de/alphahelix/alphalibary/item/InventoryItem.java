package de.alphahelix.alphalibary.item;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.alphalibary.utils.Util;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

/**
 * Modified version of an {@link ItemStack} to save it inside the {@link SimpleFile}
 */
public class InventoryItem implements Serializable {

    private ItemStack itemStack;
    private int slot;

    public InventoryItem(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public InventoryItem setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public int getSlot() {
        return slot;
    }

    public InventoryItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem that = (InventoryItem) o;
        return getSlot() == that.getSlot() &&
                Util.isSame(getItemStack(), that.getItemStack());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getItemStack(), getSlot());
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "itemStack=" + itemStack +
                ", slot=" + slot +
                '}';
    }
}
