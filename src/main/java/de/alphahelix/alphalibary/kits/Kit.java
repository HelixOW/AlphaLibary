package de.alphahelix.alphalibary.kits;

import de.alphahelix.alphalibary.events.kit.KitReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Kit implements Serializable {

    private static transient ArrayList<Kit> kits = new ArrayList<>();

    private String name;
    private String rawName;
    private int price;
    private ItemStack icon;
    private ArrayList<ItemStack> items = new ArrayList<>();

    public Kit(String name, int price, ItemStack icon, ItemStack... items) {
        this.name = name;
        this.rawName = ChatColor.stripColor(name).replace(" ", "_");
        this.price = price;
        this.icon = icon;
        Collections.addAll(this.items, items);

        if (getKitByName(name) == null) kits.add(this);
    }

    public static Kit getKitByName(String name) {
        for (Kit k : kits) {
            if (k.getRawName().equalsIgnoreCase(ChatColor.stripColor(name))) return k;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Kit setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public void giveItems(Player p) {
        for (ItemStack itemStack : items) {
            if (itemStack == null) continue;
            p.getInventory().addItem(itemStack);
        }
        Bukkit.getPluginManager().callEvent(new KitReceiveEvent(p, this));
    }

    @Override
    public String toString() {
        return "Kit{" +
                "name='" + name + '\'' +
                ", rawName='" + rawName + '\'' +
                ", price=" + price +
                ", icon=" + icon +
                ", items=" + items +
                '}';
    }
}
