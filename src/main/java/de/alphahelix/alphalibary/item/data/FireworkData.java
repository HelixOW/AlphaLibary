/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.item.data;

import de.alphahelix.alphalibary.item.SimpleFireworkEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class FireworkData implements ItemData {
    private final List<FireworkEffect> allEffects = new ArrayList<>();

    /**
     * Creates a new {@link FireworkData} with an array of {@link SimpleFireworkEffect}
     *
     * @param effects an Array of {@link SimpleFireworkEffect}s
     */
    public FireworkData(SimpleFireworkEffect... effects) {
        for (SimpleFireworkEffect effect : effects) {
            allEffects.add(effect.build());
        }
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        try {
            if (!(applyOn.getType() == Material.FIREWORK)) {
                return;
            }

            FireworkMeta fireworkMeta = (FireworkMeta) applyOn.getItemMeta();
            fireworkMeta.addEffects(allEffects);
            applyOn.setItemMeta(fireworkMeta);
        } catch (Exception e) {
            try {
                throw new WrongDataException(this);
            } catch (WrongDataException ignored) {

            }
        }
    }

    @Override
    public String toString() {
        return "FireworkData{" +
                "allEffects=" + allEffects +
                '}';
    }
}
