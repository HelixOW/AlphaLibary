package de.alphahelix.alphalibary.item;

import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

/**
 * Modified version of an {@link ItemStack} to save it inside the {@link SimpleFile}
 */
public interface InventoryItem extends Serializable {
    ItemStack getItemStack();

    int getSlot();
}
