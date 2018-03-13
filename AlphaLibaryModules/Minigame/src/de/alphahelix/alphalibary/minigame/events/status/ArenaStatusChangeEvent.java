package de.alphahelix.alphalibary.minigame.events.status;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.minigame.status.ArenaStatus;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ArenaStatusChangeEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final ArenaStatus newArenaState;
	private final ArenaStatus oldArenaState;
	private boolean cancel;
	
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
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getNewArenaState(), getOldArenaState());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ArenaStatusChangeEvent that = (ArenaStatusChangeEvent) o;
		return Objects.equal(getNewArenaState(), that.getNewArenaState()) &&
				Objects.equal(getOldArenaState(), that.getOldArenaState());
	}
	
	@Override
	public String toString() {
		return "ArenaStatusChangeEvent{" +
				"newArenaState=" + newArenaState +
				", oldArenaState=" + oldArenaState +
				'}';
	}
	
	public ArenaStatus getNewArenaState() {
		return newArenaState;
	}
	
	public ArenaStatus getOldArenaState() {
		return oldArenaState;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
