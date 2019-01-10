package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enchantments);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EnchantStorageData that = (EnchantStorageData) o;
		return Objects.equals(enchantments, that.enchantments);
	}

	@Override
	public String toString() {
		return "EnchantStorageData{" +
				"enchantments=" + enchantments +
				'}';
	}
}
