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

import de.alphahelix.alphalibary.fakeapi.instances.FakeMob;
import de.alphahelix.alphalibary.file.SimpleJSONFile;

import java.util.ArrayList;

public class MobLocationsFile extends SimpleJSONFile {

    public MobLocationsFile() {
        super("plugins/AlphaLibary", "fake_mobs.json");
    }

    public void addMobToFile(FakeMob fakeMob) {
        if (!contains(fakeMob.getUUID().toString())) {
            setValue(fakeMob.getUUID().toString(), fakeMob);
        }
    }

    public ArrayList<FakeMob> getFakeMobsFromFile() {
        ArrayList<FakeMob> fakeMobs = new ArrayList<>();

        for (String ids : getPaths()) {
            fakeMobs.add(getValue(ids, FakeMob.class));
        }
        return fakeMobs;
    }
}
