package io.github.alphahelixdev.alpary.utilities.item.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
public class EnchantStorageData implements ItemData {
	
	private final Map<Enchantment, Integer> enchantments = new HashMap<>();
	
	public EnchantStorageData(Enchantment enchantment, int level) {
		this.enchantments.put(enchantment, level);
	}
	
	@Override
	public void applyOn(ItemStack applyOn) {
		ItemMeta meta = applyOn.getItemMeta();
		
		if(meta instanceof EnchantmentStorageMeta) {
			EnchantmentStorageMeta enchStorage = (EnchantmentStorageMeta) meta;
			
			getEnchantments().forEach((key, value) -> enchStorage.addStoredEnchant(key, value, true));
			
			applyOn.setItemMeta(enchStorage);
		}
	}
}
