package de.alphahelix.alphalibary.minigame.events.countdown;

import de.alphahelix.alphalibary.minigame.countdown.GameCountdown;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountDownFinishEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final GameCountdown countdown;
	private boolean cancel;
	
	public CountDownFinishEvent(GameCountdown countdown) {
		this.countdown = countdown;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}
	
	public GameCountdown getCountdown() {
		return countdown;
	}
	
	@Override
	public String toString() {
		return "CountDownFinishEvent{" +
				"countdown=" + countdown +
				'}';
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
