package io.github.alphahelixdev.alpary.input.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ArmorChangeEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final double oldValue;
	private final double newValue;
	private boolean cancel;
	
	public ArmorChangeEvent(Player who, double oldValue, double newValue) {
		super(who);
		this.oldValue = oldValue;
		this.newValue = newValue;
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
		return cancel;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
