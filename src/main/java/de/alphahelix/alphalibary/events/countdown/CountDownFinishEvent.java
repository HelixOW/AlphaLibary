package de.alphahelix.alphalibary.events.countdown;

import de.alphahelix.alphalibary.countdown.GameCountdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountDownFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameCountdown countdown;

    public CountDownFinishEvent(GameCountdown countdown) {
        this.countdown = countdown;
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
}
