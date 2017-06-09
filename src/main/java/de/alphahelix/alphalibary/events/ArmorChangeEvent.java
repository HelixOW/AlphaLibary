package de.alphahelix.alphalibary.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ArmorChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private double oV, nV;

    public ArmorChangeEvent(Player who, double oldValue, double newValue) {
        super(who);
        this.oV = oldValue;
        this.nV = newValue;
    }

    public double getOldValue() {
        return oV;
    }

    public double getNewValue() {
        return nV;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }
}
