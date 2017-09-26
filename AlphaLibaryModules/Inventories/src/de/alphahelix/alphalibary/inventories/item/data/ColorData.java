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
package de.alphahelix.alphalibary.inventories.item.data;

import com.google.common.base.Objects;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ColorData implements ItemData {

    private int red = 0;
    private int blue = 0;
    private int green = 0;
    private DyeColor dyeColor = DyeColor.WHITE;

    /**
     * Creates a new {@link ColorData} with RGB
     *
     * @param red   the amount of red
     * @param blue  the amount of blue
     * @param green the amount of green
     */
    public ColorData(int red, int blue, int green) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        dyeColor = DyeColor.getByColor(Color.fromRGB(red, green, blue));
    }

    /**
     * Creates a new {@link ColorData} from an existing {@link DyeColor}
     *
     * @param color the existing {@link DyeColor}
     */
    public ColorData(DyeColor color) {
        this.dyeColor = color;
    }

    @Deprecated
    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {

        ItemMeta meta = applyOn.getItemMeta();

        if (meta instanceof LeatherArmorMeta) {
            try {
                LeatherArmorMeta armor = (LeatherArmorMeta) meta;

                armor.setColor(Color.fromRGB(red, green, blue));

                applyOn.setItemMeta(armor);
            } catch (IllegalArgumentException e) {
                throw new WrongDataException(this);
            }

        } else if (applyOn.getType() == Material.STAINED_CLAY
                || applyOn.getType() == Material.STAINED_GLASS
                || applyOn.getType() == Material.STAINED_GLASS_PANE
                || applyOn.getType() == Material.WOOL
                || applyOn.getType() == Material.CARPET
                || applyOn.getType() == Material.INK_SACK
                || applyOn.getType() == Material.BANNER) {

            applyOn.setDurability((applyOn.getType() == Material.INK_SACK || applyOn.getType() == Material.BANNER) ? dyeColor.getDyeData() : dyeColor.getWoolData());
        } else {
            throw new WrongDataException(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorData colorData = (ColorData) o;
        return red == colorData.red &&
                blue == colorData.blue &&
                green == colorData.green &&
                dyeColor == colorData.dyeColor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(red, blue, green, dyeColor);
    }

    @Override
    public String toString() {
        return "ColorData{" +
                "red=" + red +
                ", blue=" + blue +
                ", green=" + green +
                ", dyeColor=" + dyeColor +
                "}";
    }
}