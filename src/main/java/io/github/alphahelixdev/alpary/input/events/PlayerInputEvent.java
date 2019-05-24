package io.github.alphahelixdev.alpary.input.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
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
}
