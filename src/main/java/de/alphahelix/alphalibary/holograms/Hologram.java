/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.holograms;

import de.alphahelix.alphalibary.fakeapi.instances.FakeArmorstand;
import de.alphahelix.alphalibary.fakeapi.utils.ArmorstandFakeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Hologram implements Listener {

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
}