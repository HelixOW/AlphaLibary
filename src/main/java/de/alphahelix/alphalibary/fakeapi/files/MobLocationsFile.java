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

import de.alphahelix.alphalibary.fakeapi.FakeMobType;
import de.alphahelix.alphalibary.fakeapi.instances.FakeMob;
import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.Location;

import java.util.HashMap;

public class MobLocationsFile extends SimpleFile {

    public MobLocationsFile() {
        super("plugins/AlphaLibary", "fake_mobs.yml");
    }

    /**
     * Adds a new {@link FakeMob} to the file
     *
     * @param loc         {@link Location} where the {@link FakeMob} is located at
     * @param name        of the {@link FakeMob}
     * @param fakeMobType the {@link FakeMobType} which is spawned
     */
    public void addMobToFile(Location loc, String name, FakeMobType fakeMobType) {
        if (!configContains(name)) {
            setDefault(name.replace(" ", "_").replace("§", "&") + ".type", fakeMobType.name());
            setLocation(name.replace(" ", "_").replace("§", "&") + ".loc", loc);
        }
    }

    /**
     * Gets all {@link FakeMob} from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketMobLocations() {
        HashMap<String, Location> locationMap = new HashMap<>();

        for (String names : getKeys(false)) {
            locationMap.put(names.replace("_", " ").replace("&", "§"), getLocation(names + ".loc", false));
        }
        return locationMap;
    }

    /**
     * Gets all {@link FakeMobType}s of the {@link FakeMob}s from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link FakeMobType}s as values
     */
    public HashMap<String, FakeMobType> getPacketMobTypes() {
        HashMap<String, FakeMobType> typeMap = new HashMap<>();

        for (String names : getKeys(false)) {
            typeMap.put(names.replace("_", " ").replace("&", "§"), FakeMobType.valueOf(getString(names + ".type")));
        }
        return typeMap;
    }

}
