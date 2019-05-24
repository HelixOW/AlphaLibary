package io.github.alphahelixdev.alpary.game.events;

import io.github.alphahelixdev.alpary.game.GameKit;
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
public class KitReceiveEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final GameKit receivedGameKit;
	private boolean cancel;
	
	public KitReceiveEvent(Player who, GameKit receivedGameKit) {
		super(who);
		this.receivedGameKit = receivedGameKit;
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
