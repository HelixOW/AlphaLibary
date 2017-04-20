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

import de.alphahelix.alphalibary.fakeapi.instances.FakeBigItem;
import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.alphalibary.utils.SerializationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BigItemLocationsFile extends SimpleFile {

    private SerializationUtil<ItemStack> serializeItemStacks = new SerializationUtil<>();

    public BigItemLocationsFile() {
        super("plugins/AlphaLibary", "fake_bigitems.yml");
    }

    /**
     * Adds a new {@link FakeBigItem} to the file
     *
     * @param loc       {@link Location} where the {@link FakeBigItem} is located at
     * @param name      of the {@link FakeBigItem}
     * @param itemStack the {@link ItemStack} which is shown
     */
    public void addBigItemToFile(Location loc, String name, ItemStack itemStack) {
        if (!configContains(name)) {
            setDefault(name.replace(" ", "_").replace("§", "&") + ".item", SerializationUtil.jsonToString(serializeItemStacks.serialize(itemStack)));
            setLocation(name.replace(" ", "_").replace("§", "&") + ".loc", loc);
        }
    }

    /**
     * Gets all {@link FakeBigItem} from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketBigItemsLocations() {
        HashMap<String, Location> locationsMap = new HashMap<>();

        for (String names : getKeys(false)) {
            locationsMap.put(names.replace("_", " ").replace("&", "§"), getLocation(names + ".loc", false));
        }
        return locationsMap;
    }

    /**
     * Gets all {@link Material}s of the {@link FakeBigItem}s from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link ItemStack}s as values
     */
    public HashMap<String, ItemStack> getPacketBigItemsTypes() {
        HashMap<String, ItemStack> typesMap = new HashMap<>();

        for (String names : getKeys(false)) {
            typesMap.put(names.replace("_", " ").replace("&", "§"), serializeItemStacks.deserialize(SerializationUtil.stringToJson(getString(names + ".item"))));
        }
        return typesMap;
    }
}
