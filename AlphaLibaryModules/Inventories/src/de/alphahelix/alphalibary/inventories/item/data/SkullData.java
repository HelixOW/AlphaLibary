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
package de.alphahelix.alphalibary.inventories.item.data;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullData implements ItemData {
	
	private String ownerName;
	
	/**
	 * Creates a {@link org.bukkit.block.Skull} with a special skin
	 *
	 * @param name of the owner
	 */
	public SkullData(String name) {
		ownerName = name;
	}
	
	@Override
	public void applyOn(ItemStack applyOn) {
		try {
			if(!(applyOn.getType() == Material.SKULL_ITEM)) {
				return;
			}
			
			applyOn.setDurability((short) 3);
			
			SkullMeta skullMeta = (SkullMeta) applyOn.getItemMeta();
			skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(ownerName)));
			applyOn.setItemMeta(skullMeta);
			
		} catch(Exception e) {
			try {
				throw new WrongDataException(this);
			} catch(WrongDataException ignored) {
			
			}
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(ownerName);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SkullData skullData = (SkullData) o;
		return Objects.equal(ownerName, skullData.ownerName);
	}
	
	@Override
	public String toString() {
		return "SkullData{" +
				"ownerName='" + ownerName + '\'' +
				'}';
	}
}