package io.github.alphahelixdev.alpary.game.events.teams;

import com.google.common.base.Objects;
import io.github.alphahelixdev.alpary.game.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;


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

    @Override
    public int hashCode() {
        return Objects.hashCode(getJoinedTeam());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamJoinEvent that = (TeamJoinEvent) o;
        return Objects.equal(getJoinedTeam(), that.getJoinedTeam());
    }

    @Override
    public String toString() {
        return "TeamJoinEvent{" +
                "joinedTeam=" + joinedTeam +
                '}';
    }

    public GameTeam getJoinedTeam() {
        return joinedTeam;
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
