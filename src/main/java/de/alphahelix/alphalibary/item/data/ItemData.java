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
package de.alphahelix.alphalibary.item.data;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public interface ItemData extends Serializable {
    /**
     * Applies the {@link ItemData} onto the {@link ItemStack}
     *
     * @param applyOn the {@link ItemStack} to apply the {@link ItemData} on
     * @throws WrongDataException when {@link ItemData} can not be putted on this {@link ItemStack}
     */
    void applyOn(ItemStack applyOn) throws WrongDataException;
}
