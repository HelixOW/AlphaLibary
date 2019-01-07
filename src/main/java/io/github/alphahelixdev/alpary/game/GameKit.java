package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.game.events.KitReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameKit {

    private static final Map<String, GameKit> KITS = new HashMap<>();

    private final String name;
    private final ItemStack[] content;
    private float price;
    private ItemStack icon;

    public GameKit(String name, float price, ItemStack icon, ItemStack... content) {
        this.name = name;
        this.price = price;
        this.icon = icon;
        this.content = content;

        KITS.put(getRawName(), this);
    }

    public static GameKit of(String rawName) {
        return KITS.getOrDefault(rawName, null);
    }

    public GameKit giveItems(Player p) {
        KitReceiveEvent kre = new KitReceiveEvent(p, this);

        Bukkit.getPluginManager().callEvent(kre);

        if (!kre.isCancelled()) {
            Arrays.stream(this.content).filter(Objects::nonNull).forEach(itemStack ->
                    p.getInventory().addItem(itemStack));
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public String getRawName() {
        return ChatColor.stripColor(this.name).replace(" ", "_");
    }

    public float getPrice() {
        return price;
    }

    public GameKit setPrice(float price) {
        this.price = price;
        return this;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public GameKit setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public ItemStack[] getContent() {
        return content;
    }
}
