/*
 *
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.input;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AnvilGUI implements InputGUI {
	
	private static final List<String> OPEN_GUIS = new ArrayList<>();
	
	public static List<String> getOpenGUIs() {
		return OPEN_GUIS;
	}
	
	@Override
	public void openGUI(Player p) {
		Inventory anvil = Bukkit.createInventory(p, InventoryType.ANVIL);
		
		anvil.setItem(0, new ItemStack(Material.PAPER));
		
		OPEN_GUIS.add(p.getName());
		
		p.openInventory(anvil);
	}
}
