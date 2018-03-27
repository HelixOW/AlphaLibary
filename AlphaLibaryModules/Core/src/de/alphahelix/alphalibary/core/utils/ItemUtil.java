package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemUtil {
	
	static boolean isSame(ItemStack a, ItemStack b) {
		return AbstractItemUtil.instance.isSame(a, b);
	}
	
	static boolean isSameMeta(ItemMeta a, ItemMeta b) {
		return AbstractItemUtil.instance.isSameMeta(a, b);
	}
	
}
