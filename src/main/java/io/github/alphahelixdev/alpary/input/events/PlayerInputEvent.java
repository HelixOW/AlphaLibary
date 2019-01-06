package io.github.alphahelixdev.alpary.input.events;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;


public class PlayerInputEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final String input;

    public PlayerInputEvent(Player who, String input) {
        super(who);
        this.input = input;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInput());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInputEvent that = (PlayerInputEvent) o;
        return Objects.equal(getInput(), that.getInput());
    }

    @Override
    public String toString() {
        return "PlayerInputEvent{" +
                "input='" + input + '\'' +
                '}';
    }

    public String getInput() {
        return input;
    }
}
