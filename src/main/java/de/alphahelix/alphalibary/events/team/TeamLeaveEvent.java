package de.alphahelix.alphalibary.events.team;

import de.alphahelix.alphalibary.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TeamLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private GameTeam leftTeam;

    public TeamLeaveEvent(Player who, GameTeam leftTeam) {
        super(who);
        this.leftTeam = leftTeam;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public GameTeam getLeftTeam() {
        return leftTeam;
    }

}
