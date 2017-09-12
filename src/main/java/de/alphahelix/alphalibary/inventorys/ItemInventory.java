package de.alphahelix.alphalibary.inventorys;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.alphalibary.item.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Modified version of an {@link Inventory} to save it inside the {@link SimpleFile}
 */
public class ItemInventory implements Serializable {

    private final Inventory inventory;
    private final InventoryItem[] items;

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

                iitems.add(new InventoryItem(stack, inventory.first(stack)));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemInventory that = (ItemInventory) o;
        return Objects.equal(getInventory(), that.getInventory()) &&
                Objects.equal(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInventory(), getItems());
    }

    @Override
    public String toString() {
        return "ItemInventory{" +
                "inventory=" + inventory +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}
