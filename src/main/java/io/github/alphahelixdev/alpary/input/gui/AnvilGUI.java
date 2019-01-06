package io.github.alphahelixdev.alpary.input.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AnvilGUI implements InputGUI {

    private static final List<String> OPEN_GUIS = new ArrayList<>();

    public static List<String> getOpenGUIs() {
        return AnvilGUI.OPEN_GUIS;
    }

    @Override
    public void openGUI(Player p) {
        Inventory anvil = Bukkit.createInventory(p, InventoryType.ANVIL);

        anvil.setItem(0, new ItemStack(Material.PAPER));

        AnvilGUI.getOpenGUIs().add(p.getName());

        p.openInventory(anvil);
    }
}
