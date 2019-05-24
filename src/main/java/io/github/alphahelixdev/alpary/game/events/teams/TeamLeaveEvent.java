package io.github.alphahelixdev.alpary.game.events.teams;

import io.github.alphahelixdev.alpary.game.GameTeam;
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
public class TeamLeaveEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final GameTeam leftTeam;
	private boolean cancel;
	
	public TeamLeaveEvent(Player who, GameTeam leftTeam) {
		super(who);
		this.leftTeam = leftTeam;
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
