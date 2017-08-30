package de.alphahelix.alphalibary.fakeapi.events;

import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;
import de.alphahelix.alphalibary.nms.enums.REnumAction;
import de.alphahelix.alphalibary.nms.enums.REnumHand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class FakeEntityClickEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private FakeEntity fakeArmorstand;
    private REnumAction clickAction;
    private REnumHand hand;

    public FakeEntityClickEvent(Player who, FakeEntity fakeArmorstand, REnumAction clickAction, REnumHand hand) {
        super(who);
        this.fakeArmorstand = fakeArmorstand;
        this.clickAction = clickAction;
        this.hand = hand;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    public final static HandlerList getHandlerList() {
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
        return fakeArmorstand;
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
}
