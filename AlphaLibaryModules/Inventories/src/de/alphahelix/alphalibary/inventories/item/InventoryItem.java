package de.alphahelix.alphalibary.inventories.item;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

/**
 * Modified version of an {@link ItemStack}
 */
public class InventoryItem implements Serializable {
	
	private ItemStack itemStack;
	private int slot;
	
	public InventoryItem(ItemStack itemStack, int slot) {
		this.itemStack = itemStack;
		this.slot = slot;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getItemStack(), getSlot());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		InventoryItem that = (InventoryItem) o;
		return getSlot() == that.getSlot() &&
				ItemUtil.isSame(getItemStack(), that.getItemStack());
	}
	
	@Override
	public String toString() {
		return "InventoryItem{" +
				"itemStack=" + itemStack +
				", slot=" + slot +
				'}';
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public InventoryItem setSlot(int slot) {
		this.slot = slot;
		return this;
	}
	
	public InventoryItem setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}
}
