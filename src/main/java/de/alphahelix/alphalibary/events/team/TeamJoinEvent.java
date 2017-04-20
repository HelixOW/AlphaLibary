package de.alphahelix.alphalibary.events.team;

import de.alphahelix.alphalibary.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TeamJoinEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private GameTeam joinedTeam;

    public TeamJoinEvent(Player who, GameTeam joinedTeam) {
        super(who);
        this.joinedTeam = joinedTeam;
    }

    public final static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public GameTeam getJoinedTeam() {
        return joinedTeam;
    }
}
