package de.alphahelix.alphalibary.inventories.inventory;

import de.alphahelix.alphalibary.core.SimpleListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager extends SimpleListener {

    private static final Map<UUID, AdvancedInventory> INVENTORIES = new ConcurrentHashMap<>();
    private static InventoryManager instance;

    public InventoryManager() {
        super();
        instance = this;
    }

    public static InventoryManager getInventoryManager() {
        return instance;
    }

    public void openInventory(Player p, AdvancedInventory inventory) {
        INVENTORIES.put(p.getUniqueId(), inventory);
    }

    public void closeInventory(Player p) {
        if (INVENTORIES.containsKey(p.getUniqueId())) {
            INVENTORIES.remove(p.getUniqueId());
        }
    }

    public Optional<AdvancedInventory> getInventory(Player p) {
        return Optional.ofNullable(INVENTORIES.get(p.getUniqueId()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;

        Optional<AdvancedInventory> optInv = getInventory((Player) e.getWhoClicked());

        if (!optInv.isPresent()) return;

        AdvancedInventory inv = optInv.get();

        IDHolder idHolder = (IDHolder) e.getClickedInventory().getHolder();

        if (!idHolder.getId().equals(inv.getId())) return;

        Optional<ClickItem> optItem = inv.getItem(e.getRawSlot());

        optItem.ifPresent(item -> item.run(e));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Optional<AdvancedInventory> optInv = getInventory((Player) e.getPlayer());

        if (!optInv.isPresent()) return;

        Player p = (Player) e.getPlayer();

        AdvancedInventory inv = optInv.get();

        IDHolder idHolder = (IDHolder) e.getInventory().getHolder();

        if (!idHolder.getId().equals(inv.getId())) return;

        closeInventory((Player) e.getPlayer());
    }
}
