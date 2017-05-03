/*
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
 */

package de.alphahelix.alphalibary.fakeapi.instances;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class FakeBigItem extends FakeEntity {

    private ItemStack itemStack;

    public FakeBigItem(Location location, String name, Object nmsEntity, ItemStack itemStack) {
        super(location, name, nmsEntity);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return "FakeBigItem{" +
                "itemStack=" + itemStack +
                "} " + super.toString();
    }
}
