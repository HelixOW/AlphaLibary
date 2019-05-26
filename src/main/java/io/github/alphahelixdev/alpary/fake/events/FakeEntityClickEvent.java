package io.github.alphahelixdev.alpary.fake.events;

import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RAction;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RHand;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class FakeEntityClickEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	private final FakeEntity fakeEntity;
    private final RAction clickAction;
    private final RHand hand;

    public FakeEntityClickEvent(Player who, FakeEntity fakeEntity, RAction clickAction, RHand hand) {
		super(who);
		this.fakeEntity = fakeEntity;
		this.clickAction = clickAction;
		this.hand = hand;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	/**
	 * Gets a list of handlers handling this event.
	 *
	 * @return A list of handlers handling this event.
	 */
	@Override
	public final HandlerList getHandlers() {
		return FakeEntityClickEvent.handlers;
	}
}
