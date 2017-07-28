package de.alphahelix.alphalibary.events.status;

import de.alphahelix.alphalibary.status.ArenaStatus;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStatusChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private ArenaStatus newArenaState;
    private ArenaStatus oldArenaState;

    public ArenaStatusChangeEvent(ArenaStatus newArenaState, ArenaStatus oldArenaState) {
        this.newArenaState = newArenaState;
        this.oldArenaState = oldArenaState;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public ArenaStatus getNewArenaState() {
        return newArenaState;
    }

    public ArenaStatus getOldArenaState() {
        return oldArenaState;
    }
}
