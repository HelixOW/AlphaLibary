package io.github.alphahelixdev.alpary.utilities.menu;

import io.github.alphahelixdev.alpary.annotations.BukkitListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@BukkitListener
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class MenuManager implements Listener {
	
	private final Map<String, Menu> menus = new HashMap<>();

    public boolean hasMenuOpened(Player p) {
	    return getMenus().containsKey(p.getName());
    }

    public void toggleMenu(Player p, Menu menu) {
	    if(getMenus().containsKey(p.getName()))
		    getMenus().remove(p.getName(), menu);
        else
		    getMenus().put(p.getName(), menu);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
	    getMenus().remove(e.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMenuClick(InventoryClickEvent e) {
        Inventory i = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
	
	    if(i == null || e.getCurrentItem() == null || !getMenus().containsKey(p.getName())) return;
        if (i.getTitle().equals("")) return;

        Menu menu = this.getMenu(p);
        if (menu != null && menu.getElement(e.getRawSlot()) != null)
            menu.getElement(e.getRawSlot()).click(e);
    }
	
	public Menu getMenu(Player p) {
		return getMenus().getOrDefault(p.getName(), null);
    }
}
