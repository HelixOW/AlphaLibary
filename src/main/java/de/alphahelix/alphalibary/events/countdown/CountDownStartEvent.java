package de.alphahelix.alphalibary.events.countdown;

import de.alphahelix.alphalibary.countdown.GameCountdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountDownStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameCountdown countdown;
    private long timeTillEnd;

    public CountDownStartEvent(GameCountdown countdown, long timeTillEnd) {
        this.countdown = countdown;
        this.timeTillEnd = timeTillEnd;
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

    public long getTimeTillEnd() {
        return timeTillEnd;
    }
}
