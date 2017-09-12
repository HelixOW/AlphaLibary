package de.alphahelix.alphalibary.events.team;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TeamLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final GameTeam leftTeam;

    public TeamLeaveEvent(Player who, GameTeam leftTeam) {
        super(who);
        this.leftTeam = leftTeam;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public GameTeam getLeftTeam() {
        return leftTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamLeaveEvent that = (TeamLeaveEvent) o;
        return Objects.equal(getLeftTeam(), that.getLeftTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLeftTeam());
    }

    @Override
    public String toString() {
        return "TeamLeaveEvent{" +
                "leftTeam=" + leftTeam +
                '}';
    }
}
