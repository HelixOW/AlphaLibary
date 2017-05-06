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

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class SimplePotionEffect {

    private int durationInSec = 0;
    private PotionEffectType potionType = PotionEffectType.SPEED;
    private int amplifier = 0;

    /**
     * Creates a new {@link SimplePotionEffect} to modify {@link org.bukkit.potion.Potion}s
     *
     * @param durationInSec the amount of seconds the {@link org.bukkit.potion.Potion} should stay active
     * @param type          the {@link PotionEffectType} of the {@link org.bukkit.potion.Potion}
     * @param amplifier     the level of the {@link org.bukkit.potion.Potion}
     */
    public SimplePotionEffect(int durationInSec, PotionEffectType type, int amplifier) {
        this.durationInSec = durationInSec;
        this.potionType = type;
        this.amplifier = amplifier;
    }

    public PotionEffect createEffect() {
        return new PotionEffect(this.potionType, this.durationInSec * 20, this.amplifier);
    }

    @Override
    public String toString() {
        return "SimplePotionEffect{" +
                "durationInSec=" + durationInSec +
                ", potionType=" + potionType +
                ", amplifier=" + amplifier +
                '}';
    }
}
