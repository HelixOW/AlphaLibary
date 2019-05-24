package io.github.alphahelixdev.alpary.game.events.countdown;

import io.github.alphahelixdev.alpary.game.GameCountdown;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class CountDownStartEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final GameCountdown countdown;
	private final long timeTillEnd;
	private boolean cancel;
	
	public CountDownStartEvent(GameCountdown countdown) {
		this.countdown = countdown;
		this.timeTillEnd = countdown.getTime();
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public final HandlerList getHandlers() {
		return handlers;
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
