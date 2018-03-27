package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Utility(implementation = IItemUtil.class)
public abstract class AbstractItemUtil {
	
	public static AbstractItemUtil instance;
	
	public abstract boolean isSame(ItemStack a, ItemStack b);
	
	public abstract boolean isSameMeta(ItemMeta a, ItemMeta b);
}
