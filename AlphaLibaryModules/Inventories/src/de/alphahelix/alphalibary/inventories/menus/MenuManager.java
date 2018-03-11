package de.alphahelix.alphalibary.inventories.menus;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utilites.SimpleListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;
import java.util.HashMap;


public class MenuManager extends SimpleListener implements Serializable {
    private HashMap<String, Menu> menuHashMap = new HashMap<>();

    public MenuManager() {
        this.menuHashMap = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
    }

    public Menu getMenu(Player p) {
        if (this.menuHashMap.containsKey(p.getName()))
            return this.menuHashMap.get(p.getName());
        return null;
    }

    public boolean hasMenuOpened(Player p) {
        return this.menuHashMap.containsKey(p.getName());
    }

    public void setMenuOpened(Player p, Menu menu) {
        this.menuHashMap.put(p.getName(), menu);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (this.menuHashMap.containsKey(e.getPlayer().getName())) {
            this.menuHashMap.remove(e.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;
        if (!this.menuHashMap.containsKey(p.getName())) return;
        if (inventory.getTitle().equals("") || inventory.getTitle().isEmpty()) return;

        Menu menu = getMenu(p);
        if (menu == null) return;
        if (menu.getElement(e.getRawSlot()) == null) return;
        menu.getElement(e.getRawSlot()).click(e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuManager that = (MenuManager) o;
        return Objects.equal(menuHashMap, that.menuHashMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(menuHashMap);
    }

    @Override
    public String toString() {
        return "MenuManager{" +
                "menuHashMap=" + menuHashMap +
                '}';
    }
}
