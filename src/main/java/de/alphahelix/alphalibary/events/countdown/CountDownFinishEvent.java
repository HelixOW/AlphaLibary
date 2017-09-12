package de.alphahelix.alphalibary.events.countdown;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.countdown.GameCountdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountDownFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final GameCountdown countdown;

    public CountDownFinishEvent(GameCountdown countdown) {
        this.countdown = countdown;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public GameCountdown getCountdown() {
        return countdown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountDownFinishEvent that = (CountDownFinishEvent) o;
        return Objects.equal(getCountdown(), that.getCountdown());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCountdown());
    }

    @Override
    public String toString() {
        return "CountDownFinishEvent{" +
                "countdown=" + countdown +
                '}';
    }
}
