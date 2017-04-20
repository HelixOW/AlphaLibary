package de.alphahelix.alphalibary.events.countdown;

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
}
