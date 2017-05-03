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

import de.alphahelix.alphalibary.fakeapi.instances.FakeEndercrystal;
import de.alphahelix.alphalibary.file.SimpleJSONFile;

import java.util.ArrayList;

public class EndercrystalLocationsFile extends SimpleJSONFile {

    public EndercrystalLocationsFile() {
        super("plugins/AlphaLibary", "fake_endercrystals.json");
    }

    public void addEndercrystalToFile(FakeEndercrystal fakeEndercrystal) {
        if (!contains(fakeEndercrystal.getUUID().toString())) {
            setValue(fakeEndercrystal.getUUID().toString(), fakeEndercrystal);
        }
    }

    public ArrayList<FakeEndercrystal> getFakeEndercrystalsFromFile() {
        ArrayList<FakeEndercrystal> fakeEndercrystals = new ArrayList<>();

        for (String ids : getPaths()) {
            fakeEndercrystals.add(getValue(ids, FakeEndercrystal.class));
        }
        return fakeEndercrystals;
    }
}
