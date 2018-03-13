package de.alphahelix.alphalibary.input.events;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ArmorChangeEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private final double oV;
	private final double nV;
	private boolean cancel;
	
	public ArmorChangeEvent(Player who, double oldValue, double newValue) {
		super(who);
		this.oV = oldValue;
		this.nV = newValue;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public double getOldValue() {
		return oV;
	}
	
	public double getNewValue() {
		return nV;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(oV, nV);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ArmorChangeEvent that = (ArmorChangeEvent) o;
		return Double.compare(that.oV, oV) == 0 &&
				Double.compare(that.nV, nV) == 0;
	}
	
	@Override
	public String toString() {
		return "ArmorChangeEvent{" +
				"oldValue=" + oV +
				", newValue=" + nV +
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
