package de.alphahelix.alphalibary.inventorys;

import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.alphalibary.item.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Modified version of an {@link Inventory} to save it inside the {@link SimpleFile}
 */
public class ItemInventory implements Serializable {

    private Inventory inventory;
    private InventoryItem[] items;

    public ItemInventory(Inventory inventory, InventoryItem... items) {
        this.inventory = inventory;
        this.items = items;
    }

    public ItemInventory(Inventory inventory) {
        this.inventory = inventory;
        ArrayList<InventoryItem> iitems = new ArrayList<>();

        int slot = 0;

        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && inventory.getItem(slot) != null) {

                iitems.add(new InventoryItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return stack;
                    }

                    @Override
                    public int getSlot() {
                        return inventory.first(stack);
                    }
                });
                inventory.removeItem(inventory.getItem(slot));
            }
            slot++;
        }

        this.items = iitems.toArray(new InventoryItem[iitems.size()]);
    }

    public ItemInventory(String name, int size, InventoryItem... items) {
        this.inventory = Bukkit.createInventory(null, size, name);
        this.items = items;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public InventoryItem[] getItems() {
        return items;
    }
}
