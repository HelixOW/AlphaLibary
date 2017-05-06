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

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

import java.util.Arrays;

public class SimpleFireworkEffect {

    private Type type = Type.BALL;
    private Color[] color = new Color[]{Color.WHITE};
    private boolean flicker = false;
    private boolean trail = false;
    private Color[] fades = new Color[]{Color.WHITE};

    /**
     * Creates a new {@link SimpleFireworkEffect} to modify {@link org.bukkit.entity.Firework}s
     *
     * @param type the {@link Type} of the {@link org.bukkit.entity.Firework}
     */
    public SimpleFireworkEffect(Type type) {
        this.type = type;
    }

    public SimpleFireworkEffect setColor(Color... color) {
        this.color = color;
        return this;
    }

    public SimpleFireworkEffect setFade(Color... fades) {
        this.fades = fades;
        return this;
    }

    public SimpleFireworkEffect setTrail(boolean trail) {
        this.trail = trail;
        return this;
    }

    public SimpleFireworkEffect setFlicker(boolean flicker) {
        this.flicker = flicker;
        return this;
    }

    public FireworkEffect build() {
        return FireworkEffect.builder().flicker(flicker).trail(trail).withColor(color).withFade(fades).with(type).build();
    }

    @Override
    public String toString() {
        return "SimpleFireworkEffect{" +
                "type=" + type +
                ", color=" + Arrays.toString(color) +
                ", flicker=" + flicker +
                ", trail=" + trail +
                ", fades=" + Arrays.toString(fades) +
                '}';
    }
}