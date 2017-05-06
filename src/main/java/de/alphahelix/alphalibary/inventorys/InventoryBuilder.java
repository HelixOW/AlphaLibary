package de.alphahelix.alphalibary.inventorys;

import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.item.ItemBuilder;
import de.alphahelix.alphalibary.listener.SimpleListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public abstract class InventoryBuilder extends SimpleListener implements Serializable {

    private transient Inventory based;
    private String name;
    private int size;
    private ArrayList<SimpleItem> items = new ArrayList<>();

    public InventoryBuilder(String name, int size, SimpleItem... items) {
        super();
        this.name = name;
        this.size = size;
        Collections.addAll(this.items, items);
    }

    public String getName() {
        return name;
    }

    public InventoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public int getSize() {
        return size;
    }

    public InventoryBuilder setSize(int size) {
        this.size = size > 54 ? 54 : size;
        return this;
    }

    public InventoryBuilder addItem(SimpleItem item) {
        items.add(item);
        return this;
    }

    public InventoryBuilder removeItem(SimpleItem item) {
        items.remove(item);
        return this;
    }

    public abstract void onOpen(InventoryOpenEvent e);

    public abstract void onClose(InventoryCloseEvent e);

    public Inventory build() {
        Validate.notNull(name, "The name has to be set!");
        Validate.notNull(size, "The size has to be a multiple of 9");

        based = Bukkit.createInventory(null, size, name);

        for (SimpleItem item : items) {
            based.setItem(item.slot, item.build());
        }

        return based;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Validate.notNull(based, "The Inventory hasn't been built yet!");

        if (based.getTitle().equals(e.getInventory().getTitle())) {
            if (based.getSize() == e.getInventory().getSize())
                onOpen(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Validate.notNull(based, "The Inventory hasn't been built yet!");

        if (based.getTitle().equals(e.getInventory().getTitle()))
            if (based.getSize() == e.getInventory().getSize())
                onClose(e);
    }

    public static abstract class SimpleItem extends ItemBuilder implements Listener {

        private int slot;

        public SimpleItem(Material material, int slot) {
            super(material);
            this.slot = slot;
            Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
        }

        public SimpleItem(ItemStack is, int slot) {
            super(is);
            this.slot = slot;
            Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
        }

        public SimpleItem(String material, int slot) {
            super(material);
            this.slot = slot;
            Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
        }

        public abstract void onClick(InventoryClickEvent e);

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e) {
            if (e.getClickedInventory() == null) return;
            if (e.getSlot() == slot) {
                onClick(e);
            }
        }

        @Override
        public String toString() {
            return "SimpleItem{" +
                    "slot=" + slot +
                    "} " + super.toString();
        }
    }

    @Override
    public String toString() {
        return "InventoryBuilder{" +
                "based=" + based +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", items=" + items +
                '}';
    }
}
