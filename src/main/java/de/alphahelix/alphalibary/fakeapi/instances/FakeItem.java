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

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.Material;

public class FakeItem extends FakeEntity {

    private final Material type;

    public FakeItem(Location location, String name, Object fake, Material type) {
        super(location, name, fake);
        this.type = type;
    }

    /**
     * Gets the Material of the {@link FakeItem}
     *
     * @return the Material of the {@link FakeItem}
     */
    public Material getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FakeItem fakeItem = (FakeItem) o;
        return getType() == fakeItem.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getType());
    }

    @Override
    public String toString() {
        return "FakeItem{" +
                "type=" + type +
                "} " + super.toString();
    }
}
