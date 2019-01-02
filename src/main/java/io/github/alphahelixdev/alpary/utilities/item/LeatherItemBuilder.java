package io.github.alphahelixdev.alpary.utilities.item;


import io.github.alphahelixdev.alphalibary.AlphaLibary;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @see io.github.alphahelixdev.alphalibary.inventories.item.data.ColorData
 * @deprecated
 */

public class LeatherItemBuilder extends ItemBuilder implements Serializable {

    private Color color = Color.BLACK;

    /**
     * Create a new ItemStack with the given {@code Material}
     *
     * @param material the Material of the ItemStack
     */
    public LeatherItemBuilder(AlphaLibary libary, Material material) {
        super(libary, material);
    }

    /**
     * Create a new ItemStack out of a other ItemStack
     *
     * @param is the ItemStack which you want to edit
     */
    public LeatherItemBuilder(AlphaLibary libary, ItemStack is) {
        super(libary, is);
    }

    /**
     * Create a new ItemStack with a {@link String}
     *
     * @param material the material as a {@link String}
     */
    public LeatherItemBuilder(AlphaLibary libary, String material) {
        super(libary, material);
    }

    /**
     * Gets the custom Color of the {@link ItemStack}
     *
     * @return the custom Color of the {@link ItemStack}
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set a custom color for the {@link ItemStack}
     *
     * @param newColor the new custom color of the {@link ItemStack}
     * @return this {@link LeatherItemBuilder}
     */
    public LeatherItemBuilder setColor(Color newColor) {
        this.color = newColor;
        return this;
    }

    /**
     * Get the final {@link ItemStack} with all the attributes you have been adding
     *
     * @return the {@link ItemStack} of this {@link ItemBuilder}
     */
    @Override
    public ItemStack build() {
        ItemStack s = new ItemStack(this.getMaterial());
        s.setAmount(this.getAmount());
        s.setDurability(this.getDamage());
        LeatherArmorMeta m = (LeatherArmorMeta) s.getItemMeta();

        for (ItemFlag iflag : this.getItemflags()) {
            m.addItemFlags(iflag);
        }
        m.setDisplayName(this.getName());
        m.setLore(this.getLore());
        m.setColor(this.getColor());
        s.setItemMeta(m);
        for (Map.Entry<Enchantment, Integer> temp : this.getEnchantments().entrySet()) {
            s.addUnsafeEnchantment(temp.getKey(), temp.getValue());
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LeatherItemBuilder that = (LeatherItemBuilder) o;
        return Objects.equals(this.getColor(), that.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getColor());
    }


    @Override
    public String toString() {
        return "LeatherItemBuilder{" +
                "                            color=" + this.color +
                '}';
    }
}
