package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IItemUtil extends AbstractItemUtil {
	
	public boolean isSame(ItemStack a, ItemStack b) {
		if(a == null || b == null) return false;
		
		boolean type = a.getType() == b.getType();
		boolean amount = a.getAmount() == b.getAmount();
		boolean dura = a.getDurability() == b.getDurability();
		boolean itemMeta = a.hasItemMeta() == b.hasItemMeta();
		
		return (type) &&
				(amount) &&
				(dura) &&
				(itemMeta) && isSameMeta(a.getItemMeta(), b.getItemMeta());
	}
	
	public boolean isSameMeta(ItemMeta a, ItemMeta b) {
		if(a == null || b == null) return false;
		
		boolean dn = a.hasDisplayName() == b.hasDisplayName();
		boolean l = a.hasLore() == b.hasLore();
		boolean hdn = a.hasDisplayName();
		boolean hl = a.hasLore();
		
		if(dn) {
			if(l) {
				if(hdn) {
					if(hl) {
						return a.getDisplayName().equals(b.getDisplayName()) && a.getLore().equals(b.getLore());
					} else { //only name
						return a.getDisplayName().equals(b.getDisplayName());
					}
				} else { //maybe lore
					return !hl || a.getLore().equals(b.getLore());
				}
			} else { //maybe name
				return !hdn || a.getDisplayName().equals(b.getDisplayName());
			}
		} else if(l) {
			return !hl || a.getLore().equals(b.getLore());
		}
		return false;
	}
	
}
