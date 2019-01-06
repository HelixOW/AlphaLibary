package io.github.alphahelixdev.alpary.utilities.menu;

import io.github.alphahelixdev.alpary.annotations.BukkitListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@BukkitListener
public class MenuManager implements Listener {

    private final Map<String, Menu> menus;

    public MenuManager() {
        this.menus = new HashMap<>();
    }

    public boolean hasMenuOpened(Player p) {
        return this.menus.containsKey(p.getName());
    }

    public void toggleMenu(Player p, Menu menu) {
        if (this.menus.containsKey(p.getName()))
            this.menus.remove(p.getName(), menu);
        else
            this.menus.put(p.getName(), menu);
    }

    public Menu getMenu(Player p) {
        return this.menus.getOrDefault(p.getName(), null);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        this.menus.remove(e.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMenuClick(InventoryClickEvent e) {
        Inventory i = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();

        if (i == null || e.getCurrentItem() == null || !this.menus.containsKey(p.getName())) return;
        if (i.getTitle().equals("")) return;

        Menu menu = this.getMenu(p);
        if (menu != null && menu.getElement(e.getRawSlot()) != null)
            menu.getElement(e.getRawSlot()).click(e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuManager that = (MenuManager) o;
        return Objects.equals(menus, that.menus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menus);
    }

    @Override
    public String toString() {
        return "MenuManager{" +
                "menus=" + menus +
                '}';
    }
}
