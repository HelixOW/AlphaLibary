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

import de.alphahelix.alphalibary.fakeapi.instances.FakePainting;
import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.Location;

import java.util.HashMap;

public class PaintingLocationsFile extends SimpleFile {
    public PaintingLocationsFile() {
        super("plugins/AlphaLibary", "fake_painting.yml");
    }

    /**
     * Adds a new {@link FakePainting} to the file
     *
     * @param loc  {@link Location} where the {@link FakePainting} is located at
     * @param name of the {@link FakePainting}
     */
    public void addPaintingToFile(Location loc, String name) {
        if (!configContains(name)) {
            setLocation(name.replace(" ", "_").replace("§", "&"), loc);
        }
    }

    /**
     * Gets all {@link FakePainting} from the file and returns it as a {@link HashMap}
     *
     * @return the {@link HashMap} with the name as keys and {@link Location}s as values
     */
    public HashMap<String, Location> getPacketPainting() {
        HashMap<String, Location> paintingmap = new HashMap<>();

        for (String names : getKeys(false)) {
            paintingmap.put(names.replace("_", " ").replace("&", "§"), getLocation(names, false));
        }
        return paintingmap;
    }
}
