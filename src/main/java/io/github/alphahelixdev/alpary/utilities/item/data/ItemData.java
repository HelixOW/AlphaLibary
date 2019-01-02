package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public interface ItemData extends Serializable {
    /**
     * Applies the {@link ItemData} onto the {@link ItemStack}
     *
     * @param applyOn the {@link ItemStack} to apply the {@link ItemData} on
     * @throws WrongDataException when {@link ItemData} can not be putted on this {@link ItemStack}
     */
    void applyOn(ItemStack applyOn) throws WrongDataException;
}
