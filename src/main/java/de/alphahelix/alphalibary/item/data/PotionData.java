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

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class PotionData extends ItemData {

    private final ArrayList<PotionEffect> toApply = new ArrayList<>();

    /**
     * Creates a new {@link PotionData} with an array of {@link SimplePotionEffect}
     *
     * @param effects an Array of {@link SimplePotionEffect}s
     */
    public PotionData(SimplePotionEffect... effects) {
        for (SimplePotionEffect effect : effects) {
            this.toApply.add(effect.createEffect());
        }
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        try {

            PotionMeta meta = (PotionMeta) applyOn.getItemMeta();
            for (PotionEffect effect : toApply) {
                meta.addCustomEffect(effect, false);
            }
            applyOn.setItemMeta(meta);

        } catch (Exception e) {
            throw new WrongDataException(this);
        }
    }
}
