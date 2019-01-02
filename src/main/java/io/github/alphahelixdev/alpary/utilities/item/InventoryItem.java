package io.github.alphahelixdev.alpary.utilities.item;

import io.github.alphahelixdev.alphalibary.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Objects;

/**
 * Modified version of an {@link ItemStack}
 */
public class InventoryItem implements Serializable {

    private int slot;
    private ItemStack itemStack;

    public InventoryItem(ItemStack itemStack, int slot) {
        this.setItemStack(itemStack);
        this.setSlot(slot);
    }

    public int getSlot() {
        return this.slot;
    }

    public InventoryItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public InventoryItem setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem that = (InventoryItem) o;
        return this.getSlot() == that.getSlot() &&
                ItemUtil.isSame(this.getItemStack(), that.getItemStack());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSlot(), this.getItemStack());
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "                            slot=" + this.slot +
                ",                             itemStack=" + this.itemStack +
                '}';
    }
}
