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

package de.alphahelix.alphalibary.holograms;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.fakeapi.instances.FakeArmorstand;
import de.alphahelix.alphalibary.fakeapi.utils.ArmorstandFakeUtil;
import de.alphahelix.alphalibary.listener.SimpleListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;

public class Hologram extends SimpleListener implements Serializable {

    private String name;
    private Location location;

    public Hologram(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public FakeArmorstand spawn(Player player) {
        return ArmorstandFakeUtil.spawnArmorstand(player, getLocation(), getName());
    }

    public Location getLocation() {
        return location;
    }

    public Hologram setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getName() {
        return name;
    }

    public Hologram setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hologram hologram = (Hologram) o;
        return Objects.equal(getName(), hologram.getName()) &&
                Objects.equal(getLocation(), hologram.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getLocation());
    }

    @Override
    public String toString() {
        return "Hologram{" +
                "name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}