package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.event.Cancellable;

@EqualsAndHashCode
@ToString
public class CancellablePacket implements Cancellable {
	
	private boolean cancelled;
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
