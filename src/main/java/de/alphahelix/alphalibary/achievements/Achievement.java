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

package de.alphahelix.alphalibary.achievements;

import de.alphahelix.alphalibary.item.InventoryItem;

import java.io.Serializable;
import java.util.List;

public interface Achievement extends Serializable {
	
	/**
	 * Gets the name of the achievement for id purpose
	 *
	 * @return the name of the achievement
	 */
	String getName();
	
	/**
	 * Gets an ItemStack which represents the achievement inside a inventory
	 *
	 * @return the ItemStack with its corresponding slot inside a inventory
	 */
	InventoryItem getIcon();
	
	/**
	 * Gives out a short explanation of the achievement for clarity
	 *
	 * @return a short explanation of the achievement
	 */
	List<String> getDescription();
}
