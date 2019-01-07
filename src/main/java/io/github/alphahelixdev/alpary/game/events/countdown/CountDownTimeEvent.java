package io.github.alphahelixdev.alpary.game.events.countdown;

import io.github.alphahelixdev.alpary.game.GameCountdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class CountDownTimeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final GameCountdown countdown;
    private final long reachedTime;

    public CountDownTimeEvent(GameCountdown countdown, long reachedTime) {
        this.countdown = countdown;
        this.reachedTime = reachedTime;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GameCountdown getCountdown() {
        return countdown;
    }

    public long getReachedTime() {
        return reachedTime;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }
}
