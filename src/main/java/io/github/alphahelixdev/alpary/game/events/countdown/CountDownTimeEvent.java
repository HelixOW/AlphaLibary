package io.github.alphahelixdev.alpary.game.events.countdown;

import io.github.alphahelixdev.alpary.game.GameCountdown;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
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

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }
}
