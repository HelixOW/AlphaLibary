package de.alphahelix.alphalibary.events.status;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.status.ArenaStatus;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStatusChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final ArenaStatus newArenaState;
    private final ArenaStatus oldArenaState;

    public ArenaStatusChangeEvent(ArenaStatus newArenaState, ArenaStatus oldArenaState) {
        this.newArenaState = newArenaState;
        this.oldArenaState = oldArenaState;
    }

    public static HandlerList getHandlerList() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArenaStatusChangeEvent that = (ArenaStatusChangeEvent) o;
        return Objects.equal(getNewArenaState(), that.getNewArenaState()) &&
                Objects.equal(getOldArenaState(), that.getOldArenaState());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNewArenaState(), getOldArenaState());
    }

    @Override
    public String toString() {
        return "ArenaStatusChangeEvent{" +
                "newArenaState=" + newArenaState +
                ", oldArenaState=" + oldArenaState +
                '}';
    }
}
