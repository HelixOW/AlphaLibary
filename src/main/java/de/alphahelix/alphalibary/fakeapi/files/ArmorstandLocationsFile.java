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

package de.alphahelix.alphalibary.fakeapi.files;

import de.alphahelix.alphalibary.fakeapi.instances.FakeArmorstand;
import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.Location;

import java.util.HashMap;

public class ArmorstandLocationsFile extends SimpleFile {

    public ArmorstandLocationsFile() {
        super("plugins/AlphaLibary", "fake_armorstand.yml");
    }

    /**
     * Adds a new {@link FakeArmorstand} to the file
     *
     * @param loc  {@link Location} where the {@link FakeArmorstand} is located at
     * @param name of the {@link FakeArmorstand}
     */
    public void addArmorstandToFile(Location loc, String name) {
        if (!configContains(name)) {
            setLocation(name.replace(" ", "_").replace("§", "&"), loc);
        }
    }

    /**
     * Gets all {@link FakeArmorstand} from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketArmorstand() {
        HashMap<String, Location> standsMap = new HashMap<>();

        for (String names : getKeys(false)) {
            standsMap.put(names.replace("_", " ").replace("&", "§"), getLocation(names, false));
        }
        return standsMap;
    }
}
