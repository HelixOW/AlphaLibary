/*
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.inventories;

import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.SimpleListener;
import de.alphahelix.alphalibary.inventories.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;


public class SimpleMovingInventory extends SimpleListener implements Serializable {

    private static final HashMap<UUID, SimpleMovingInventory> USERS = new HashMap<>();
    private static final ArrayList<Inventory> PAGES = new ArrayList<>();
    private final String title;
    private final int size;
    private ArrayList<ItemStack> items = new ArrayList<>();
    private ItemStack nextPage = null;
    private ItemStack previousPage = null;
    private int currpage = 0;

    /**
     * Creates a new {@link Inventory} with multiple Sites
     *
     * @param p        the {@link Player} to open the {@link Inventory} for
     * @param size     the site of the {@link Inventory}
     * @param items    a {@link ArrayList} of {@link ItemStack}s which should be inside the {@link Inventory}
     * @param name     the name of the {@link Inventory}
     * @param nextPage the name for the {@link ItemStack} for the next page
     * @param prevPage the name for the {@link ItemStack} for the previous page
     */
    public SimpleMovingInventory(Player p, int size, ArrayList<ItemStack> items, String name, ItemStack nextPage, ItemStack prevPage) {
        Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
        this.title = name;
        this.items = items;
        this.size = size;
        this.nextPage = nextPage;
        this.previousPage = prevPage;

        Inventory page = getBlankPage(name, size);

        for (ItemStack item : items) {
            if (page.firstEmpty() == -1) {
                PAGES.add(page);
                page = getBlankPage(name, size);
                page.addItem(item);
            } else {
                page.addItem(item);
            }
        }

        PAGES.add(page);
        p.openInventory(PAGES.get(currpage));
        USERS.put(p.getUniqueId(), this);
    }

    public void construct(Player p) {
        Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());

        Inventory page = getBlankPage(title, size);

        for (ItemStack item : items) {
            if (page.firstEmpty() == -1) {
                PAGES.add(page);
                page = getBlankPage(title, size);
                page.addItem(item);
            } else {
                page.addItem(item);
            }
        }
        PAGES.add(page);
        p.openInventory(PAGES.get(currpage));
        USERS.put(p.getUniqueId(), this);
    }

    private Inventory getBlankPage(String name, int size) {
        Inventory inv = Bukkit.createInventory(null, size, name);

        for (int i = size - 8; i < size - 1; i++) {
            inv.setItem(i,
                    new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDamage((short) 7).build());
        }

        inv.setItem(size - 1, nextPage);
        inv.setItem(size - 9, previousPage);
        return inv;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) return;
        if (Objects.equals(e.getClickedInventory().getTitle(), title)) e.setCancelled(true);
        if (!USERS.containsKey(p.getUniqueId()))
            return;

        SimpleMovingInventory inv = USERS.get(p.getUniqueId());
        if (e.getCurrentItem() == null)
            return;
        if (e.getCurrentItem().getItemMeta() == null)
            return;
        if (e.getCurrentItem().getItemMeta().getDisplayName() == null)
            return;

        if (sameName(e.getCurrentItem(), nextPage)) {
            e.setCancelled(true);
            if (inv.currpage < PAGES.size() - 1) {
                inv.currpage += 1;
                p.openInventory(PAGES.get(inv.currpage));
            }
        } else if (sameName(e.getCurrentItem(), previousPage)) {
            e.setCancelled(true);
            if (inv.currpage > 0) {
                inv.currpage -= 1;
                p.openInventory(PAGES.get(inv.currpage));
            }
        }
    }

    public boolean sameName(ItemStack is1, ItemStack is2) {
        return ChatColor.stripColor(is1.getItemMeta().getDisplayName()).equals(ChatColor.stripColor(is2.getItemMeta().getDisplayName()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleMovingInventory that = (SimpleMovingInventory) o;
        return size == that.size &&
                currpage == that.currpage &&
                com.google.common.base.Objects.equal(items, that.items) &&
                com.google.common.base.Objects.equal(title, that.title) &&
                com.google.common.base.Objects.equal(nextPage, that.nextPage) &&
                com.google.common.base.Objects.equal(previousPage, that.previousPage);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(items, title, size, nextPage, previousPage, currpage);
    }

    @Override
    public String toString() {
        return "SimpleMovingInventory{" +
                "items=" + items +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", nextPage=" + nextPage +
                ", previousPage=" + previousPage +
                ", currpage=" + currpage +
                '}';
    }
}
