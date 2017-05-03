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
import de.alphahelix.alphalibary.file.SimpleJSONFile;

import java.util.ArrayList;

public class ItemLocationsFile extends SimpleJSONFile {

    public ItemLocationsFile() {
        super("plugins/AlphaLibary", "fake_items.json");
    }

    public void addItemToFile(FakeItem fakeItem) {
        if (!contains(fakeItem.getUUID().toString())) {
            setValue(fakeItem.getUUID().toString(), fakeItem);
        }
    }

    public ArrayList<FakeItem> getFakeItemsFromFile() {
        ArrayList<FakeItem> fakeItems = new ArrayList<>();

        for (String ids : getPaths()) {
            fakeItems.add(getValue(ids, FakeItem.class));
        }
        return fakeItems;
    }
}
