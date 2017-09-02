package de.alphahelix.alphalibary.events.countdown;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.countdown.GameCountdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountDownTimeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameCountdown countdown;
    private long reachedTime;

    public CountDownTimeEvent(GameCountdown countdown, long reachedTime) {
        this.countdown = countdown;
        this.reachedTime = reachedTime;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public GameCountdown getCountdown() {
        return countdown;
    }

    public long getReachedTime() {
        return reachedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountDownTimeEvent that = (CountDownTimeEvent) o;
        return getReachedTime() == that.getReachedTime() &&
                Objects.equal(getCountdown(), that.getCountdown());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCountdown(), getReachedTime());
    }

    @Override
    public String toString() {
        return "CountDownTimeEvent{" +
                "countdown=" + countdown +
                ", reachedTime=" + reachedTime +
                '}';
    }
}
