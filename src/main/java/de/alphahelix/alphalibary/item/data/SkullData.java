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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullData extends ItemData {

    private String ownerName = null;

    /**
     * Creates a {@link org.bukkit.block.Skull} with a special skin
     *
     * @param name of the owner
     */
    public SkullData(String name) {
        ownerName = name;
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        try {
            if (!(applyOn.getType() == Material.SKULL_ITEM)) {
                return;
            }

            applyOn.setDurability((short) 3);

            SkullMeta skullMeta = (SkullMeta) applyOn.getItemMeta();
            skullMeta.setOwner(ownerName);
            applyOn.setItemMeta(skullMeta);

        } catch (Exception e) {
            try {
                throw new WrongDataException(this);
            } catch (WrongDataException ignored) {

            }
        }
    }
}