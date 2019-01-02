package io.github.alphahelixdev.alpary.utilities.item.data;

import com.google.common.base.Objects;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ColorData implements ItemData {

    private int red = 0;
    private int blue = 0;
    private int green = 0;
    private DyeColor dyeColor;

    /**
     * Creates a new {@link ColorData} with RGB
     *
     * @param red   the amount of red
     * @param blue  the amount of blue
     * @param green the amount of green
     */
    public ColorData(int red, int blue, int green) {
        this.setRed(red);
        this.setBlue(blue);
        this.setGreen(green);
        this.setDyeColor(DyeColor.getByColor(Color.fromRGB(red, green, blue)));
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

                armor.setColor(Color.fromRGB(this.getRed(), this.getGreen(), this.getBlue()));

                applyOn.setItemMeta(armor);
            } catch (IllegalArgumentException e) {
                throw new WrongDataException(this);
            }

        } else {
            throw new WrongDataException(this);
        }
    }

    public int getRed() {
        return this.red;
    }

    public ColorData setRed(int red) {
        this.red = red;
        return this;
    }

    public int getBlue() {
        return this.blue;
    }

    public ColorData setBlue(int blue) {
        this.blue = blue;
        return this;
    }

    public int getGreen() {
        return this.green;
    }

    public ColorData setGreen(int green) {
        this.green = green;
        return this;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public ColorData setDyeColor(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getRed(), this.getBlue(), this.getGreen(), this.getDyeColor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorData colorData = (ColorData) o;
        return this.getRed() == colorData.getRed() &&
                this.getBlue() == colorData.getBlue() &&
                this.getGreen() == colorData.getGreen() &&
                this.getDyeColor() == colorData.getDyeColor();
    }

    @Override
    public String toString() {
        return "ColorData{" +
                "red=" + this.getRed() +
                ", blue=" + this.getBlue() +
                ", green=" + this.getGreen() +
                ", dyeColor=" + this.getDyeColor() +
                "}";
    }
}