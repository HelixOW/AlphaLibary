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

package de.alphahelix.alphalibary.minigame.achievements;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.inventories.item.InventoryItem;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class Achievement implements Serializable {

    private String name;
    private InventoryItem icon;
    private List<String> description;

    public Achievement(String name, InventoryItem icon, List<String> description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Achievement setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryItem getIcon() {
        return icon;
    }

    public Achievement setIcon(InventoryItem icon) {
        this.icon = icon;
        return this;
    }

    public List<String> getDescription() {
        return description;
    }

    public Achievement setDescription(String... description) {
        this.description = Arrays.asList(description);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return Objects.equal(getName(), that.getName()) &&
                Objects.equal(getIcon(), that.getIcon()) &&
                Objects.equal(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getIcon(), getDescription());
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", description=" + description +
                '}';
    }
}
