package io.github.alphahelixdev.alpary.input.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.InventoryView;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
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
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}