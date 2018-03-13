/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.inventories.item;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.Serializable;
import java.util.Map;

/**
 * @see de.alphahelix.alphalibary.inventorys.item.data.ColorData
 * @deprecated
 */

public class LeatherItemBuilder extends ItemBuilder implements Serializable {
	
	private Color color = Color.BLACK;
	
	/**
	 * Create a new ItemStack with the given {@code Material}
	 *
	 * @param material the Material of the ItemStack
	 */
	public LeatherItemBuilder(Material material) {
		super(material);
	}
	
	/**
	 * Create a new ItemStack out of a other ItemStack
	 *
	 * @param is the ItemStack which you want to edit
	 */
	public LeatherItemBuilder(ItemStack is) {
		super(is);
	}
	
	/**
	 * Create a new ItemStack with a {@link String}
	 *
	 * @param material the material as a {@link String}
	 */
	public LeatherItemBuilder(String material) {
		super(Material.getMaterial(material.toUpperCase()));
	}
	
	/**
	 * Gets the custom Color of the {@link ItemStack}
	 *
	 * @return the custom Color of the {@link ItemStack}
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Set a custom color for the {@link ItemStack}
	 *
	 * @param newColor the new custom color of the {@link ItemStack}
	 *
	 * @return this {@link LeatherItemBuilder}
	 */
	public LeatherItemBuilder setColor(Color newColor) {
		color = newColor;
		return this;
	}
	
	/**
	 * Get the final {@link ItemStack} with all the attributes you have been adding
	 *
	 * @return the {@link ItemStack} of this {@link ItemBuilder}
	 */
	@Override
	public ItemStack build() {
		ItemStack s = new ItemStack(this.getMaterial());
		s.setAmount(this.getAmount());
		s.setDurability(this.getDamage());
		LeatherArmorMeta m = (LeatherArmorMeta) s.getItemMeta();
		
		for(ItemFlag iflag : this.getAllItemflags()) {
			m.addItemFlags(iflag);
		}
		m.setDisplayName(this.getName());
		m.setLore(this.getLore());
		m.setColor(color);
		s.setItemMeta(m);
		for(Map.Entry<Enchantment, Integer> temp : this.getAllEnchantments().entrySet()) {
			s.addUnsafeEnchantment(temp.getKey(), temp.getValue());
		}
		return s;
	}
	
	@Override
	public String toString() {
		return "LeatherItemBuilder{" +
				"color=" + color +
				"} " + super.toString();
	}
}
