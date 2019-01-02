package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public interface ItemData extends Serializable {
	
	void applyOn(ItemStack applyOn) throws WrongDataException;
}
