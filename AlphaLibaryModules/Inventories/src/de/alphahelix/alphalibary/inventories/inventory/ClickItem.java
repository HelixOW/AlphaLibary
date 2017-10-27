package de.alphahelix.alphalibary.inventories.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ClickItem {

    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> event;

    public ClickItem(ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        this.itemStack = itemStack;
        this.event = event;
    }

    public static ClickItem ofNull(ItemStack itemStack) {
        return of(itemStack, inventoryClickEvent -> {
        });
    }

    public static ClickItem ofUnClickable(ItemStack itemStack) {
        return of(itemStack, inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }

    public static ClickItem ofUnClickable(ItemStack itemStack, Consumer<InventoryClickEvent> eventConsumer) {
        return of(itemStack, inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            eventConsumer.accept(inventoryClickEvent);
        });
    }

    public static ClickItem of(ItemStack itemStack, Consumer<InventoryClickEvent> eventConsumer) {
        return new ClickItem(itemStack, eventConsumer);
    }

    public void run(InventoryClickEvent e) {
        event.accept(e);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
