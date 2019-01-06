package io.github.alphahelixdev.alpary.input.events;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.InventoryView;


public class ItemRenameEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final InventoryView inventory;
    private final String name;
    private boolean cancelled;

    public ItemRenameEvent(Player who, InventoryView iw, String nN) {
        super(who);
        this.inventory = iw;
        this.name = nN;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInventory(), getName(), isCancelled());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRenameEvent that = (ItemRenameEvent) o;
        return isCancelled() == that.isCancelled() &&
                Objects.equal(getInventory(), that.getInventory()) &&
                Objects.equal(getName(), that.getName());
    }

    @Override
    public String toString() {
        return "ItemRenameEvent{" +
                "inventory=" + inventory +
                ", name='" + name + '\'' +
                ", cancelled=" + cancelled +
                '}';
    }

    public InventoryView getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}