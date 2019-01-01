package io.github.alphahelixdev.alpary.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {
	
	public boolean isSame(ItemStack a, ItemStack b) {
		if(a == null || b == null) return false;
		
		boolean type = a.getType() == b.getType();
		boolean amount = a.getAmount() == b.getAmount();
		boolean itemMeta = a.hasItemMeta() == b.hasItemMeta();
		
		return (type) &&
				(amount) &&
				(itemMeta) && isSameMeta(a.getItemMeta(), b.getItemMeta());
	}
	
	public boolean isSameMeta(ItemMeta a, ItemMeta b) {
		if(a == null || b == null) return false;
		
		boolean decide = true;
		
		boolean displayName = a.hasDisplayName() == b.hasDisplayName();
		boolean lore = a.hasLore() == b.hasLore();
		boolean damageable = (a instanceof Damageable) == (b instanceof Damageable);
		
		if(displayName)
			if(a.hasDisplayName())
				if(!a.getDisplayName().equals(b.getDisplayName()))
					decide = false;
		
		if(lore)
			if(a.hasLore())
				if(!a.getLore().equals(b.getLore()))
					decide = false;
		
		if(damageable)
			if(a instanceof Damageable)
				if(((Damageable) a).getDamage() != ((Damageable) b).getDamage())
					decide = false;
		
		return decide;
	}
	
}
