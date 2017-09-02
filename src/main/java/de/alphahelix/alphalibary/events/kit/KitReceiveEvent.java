package de.alphahelix.alphalibary.events.kit;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class KitReceiveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Kit receivedKit;

    public KitReceiveEvent(Player who, Kit receivedKit) {
        super(who);
        this.receivedKit = receivedKit;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public Kit getReceivedKit() {
        return receivedKit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KitReceiveEvent that = (KitReceiveEvent) o;
        return Objects.equal(getReceivedKit(), that.getReceivedKit());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getReceivedKit());
    }

    @Override
    public String toString() {
        return "KitReceiveEvent{" +
                "receivedKit=" + receivedKit +
                '}';
    }
}
