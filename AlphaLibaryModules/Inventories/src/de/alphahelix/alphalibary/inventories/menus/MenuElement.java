package de.alphahelix.alphalibary.inventories.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuElement {
    ItemStack getIcon(Player p);

    void click(InventoryClickEvent e);
}
