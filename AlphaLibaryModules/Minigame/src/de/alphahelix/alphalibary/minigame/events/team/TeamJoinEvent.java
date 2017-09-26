package de.alphahelix.alphalibary.minigame.events.team;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.minigame.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@SuppressWarnings("ALL")
public class TeamJoinEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final GameTeam joinedTeam;
    private boolean cancel;

    public TeamJoinEvent(Player who, GameTeam joinedTeam) {
        super(who);
        this.joinedTeam = joinedTeam;
    }

    public static HandlerList getHandlerList() {
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

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
