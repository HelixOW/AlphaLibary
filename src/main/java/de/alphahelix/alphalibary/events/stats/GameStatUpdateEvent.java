package de.alphahelix.alphalibary.events.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class GameStatUpdateEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private String updatedStat;
    private String updateValue;

    public GameStatUpdateEvent(Player who, String updatedStat, String updateValue) {
        super(who);
        this.updatedStat = updatedStat;
        this.updateValue = updateValue;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public String getUpdatedStat() {
        return updatedStat;
    }

    public String getUpdateValue() {
        return updateValue;
    }
}
