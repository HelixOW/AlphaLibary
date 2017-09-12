package de.alphahelix.alphalibary.events.countdown;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.countdown.GameCountdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountDownStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final GameCountdown countdown;
    private final long timeTillEnd;

    public CountDownStartEvent(GameCountdown countdown, long timeTillEnd) {
        this.countdown = countdown;
        this.timeTillEnd = timeTillEnd;
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

    public long getTimeTillEnd() {
        return timeTillEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountDownStartEvent that = (CountDownStartEvent) o;
        return getTimeTillEnd() == that.getTimeTillEnd() &&
                Objects.equal(getCountdown(), that.getCountdown());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCountdown(), getTimeTillEnd());
    }

    @Override
    public String toString() {
        return "CountDownStartEvent{" +
                "countdown=" + countdown +
                ", timeTillEnd=" + timeTillEnd +
                '}';
    }
}
