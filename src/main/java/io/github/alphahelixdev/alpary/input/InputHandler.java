package io.github.alphahelixdev.alpary.input;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public interface InputHandler {

    void handle(Player player, String input);

    void handle(Player player, InventoryView inventoryView, String input);

}
