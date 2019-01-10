package io.github.alphahelixdev.alpary.fake.events;

import com.google.common.base.Objects;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumAction;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumHand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class FakeEntityClickEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	private final FakeEntity fakeEntity;
	private final REnumAction clickAction;
	private final REnumHand hand;
	
	public FakeEntityClickEvent(Player who, FakeEntity fakeEntity, REnumAction clickAction, REnumHand hand) {
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
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getFakeEntity(), getClickAction(), getHand());
	}
	
	/**
	 * Gets the clicked {@link FakeEntity}
	 *
	 * @return the clicked {@link FakeEntity}
	 */
	public FakeEntity getFakeEntity() {
		return this.fakeEntity;
	}
	
	/**
	 * gets the {@link REnumAction}
	 *
	 * @return the {@link REnumAction}
	 */
	public REnumAction getClickAction() {
		return this.clickAction;
	}
	
	public REnumHand getHand() {
		return this.hand;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		FakeEntityClickEvent that = (FakeEntityClickEvent) o;
		return Objects.equal(getFakeEntity(), that.getFakeEntity()) &&
				getClickAction() == that.getClickAction() &&
				getHand() == that.getHand();
	}
	
	@Override
	public String toString() {
		return "FakeEntityClickEvent{" +
				"fakeEntity=" + fakeEntity +
				", clickAction=" + clickAction +
				", hand=" + hand +
				'}';
	}
	
}
