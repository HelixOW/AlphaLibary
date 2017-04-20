package de.alphahelix.alphalibary.events.kit;

import de.alphahelix.alphalibary.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class KitReceiveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Kit receivedKit;

    public KitReceiveEvent(Player who, Kit receivedKit) {
        super(who);
        this.receivedKit = receivedKit;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public Kit getReceivedKit() {
        return receivedKit;
    }
}
