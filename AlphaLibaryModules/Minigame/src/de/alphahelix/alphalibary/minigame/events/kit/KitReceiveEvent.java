package de.alphahelix.alphalibary.minigame.events.kit;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.minigame.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;


public class KitReceiveEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final Kit receivedKit;
	private boolean cancel;
	
	public KitReceiveEvent(Player who, Kit receivedKit) {
		super(who);
		this.receivedKit = receivedKit;
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
		return Objects.hashCode(getReceivedKit());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		KitReceiveEvent that = (KitReceiveEvent) o;
		return Objects.equal(getReceivedKit(), that.getReceivedKit());
	}
	
	@Override
	public String toString() {
		return "KitReceiveEvent{" +
				"receivedKit=" + receivedKit +
				'}';
	}
	
	public Kit getReceivedKit() {
		return receivedKit;
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
