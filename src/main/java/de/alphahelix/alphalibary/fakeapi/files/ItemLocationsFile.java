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

import de.alphahelix.alphalibary.fakeapi.instances.FakeItem;
import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;

public class ItemLocationsFile extends SimpleFile {

    public ItemLocationsFile() {
        super("plugins/AlphaLibary", "fake_items.yml");
    }

    /**
     * Adds a new {@link FakeItem} to the file
     *
     * @param loc  {@link Location} where the {@link FakeItem} is located at
     * @param name of the {@link FakeItem}
     * @param type {@link Material} of the {@link FakeItem} to save
     */
    public void addItemToFile(Location loc, String name, Material type) {
        if (!configContains(name)) {
            setDefault(name.replace(" ", "_").replace("§", "&") + ".type", type.name());
            setLocation(name.replace(" ", "_").replace("§", "&") + ".loc", loc);
        }
    }

    /**
     * Gets all {@link Location}s of the {@link FakeItem}s from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketItemsLocations() {
        HashMap<String, Location> itemMaps = new HashMap<>();

        for (String names : getKeys(false)) {
            itemMaps.put(names.replace("_", " ").replace("&", "§"), getLocation(names + ".loc", false));
        }
        return itemMaps;
    }

    /**
     * Gets all {@link Material}s of the {@link FakeItem}s from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Material}s as values
     */
    public HashMap<String, Material> getPacketItemsTypes() {
        HashMap<String, Material> itemMaps = new HashMap<>();

        for (String names : getKeys(false)) {
            itemMaps.put(names.replace("_", " ").replace("&", "§"), Material.getMaterial(getString(names + ".type")));
        }
        return itemMaps;
    }
}
