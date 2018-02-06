package de.alphahelix.alphalibary.fakeapi.events;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumAction;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumHand;
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

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
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
        return handlers;
    }

    /**
     * Gets the clicked {@link FakeEntity}
     *
     * @return the clicked {@link FakeEntity}
     */
    public FakeEntity getFakeEntity() {
        return fakeEntity;
    }

    /**
     * gets the {@link REnumAction}
     *
     * @return the {@link REnumAction}
     */
    public REnumAction getClickAction() {
        return clickAction;
    }

    public REnumHand getHand() {
        return hand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakeEntityClickEvent that = (FakeEntityClickEvent) o;
        return Objects.equal(getFakeEntity(), that.getFakeEntity()) &&
                getClickAction() == that.getClickAction() &&
                getHand() == that.getHand();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFakeEntity(), getClickAction(), getHand());
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
