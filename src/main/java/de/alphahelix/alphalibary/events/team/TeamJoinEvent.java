package de.alphahelix.alphalibary.events.team;

import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamJoinEvent that = (TeamJoinEvent) o;
        return Objects.equal(getJoinedTeam(), that.getJoinedTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getJoinedTeam());
    }

    @Override
    public String toString() {
        return "TeamJoinEvent{" +
                "joinedTeam=" + joinedTeam +
                '}';
    }
}
