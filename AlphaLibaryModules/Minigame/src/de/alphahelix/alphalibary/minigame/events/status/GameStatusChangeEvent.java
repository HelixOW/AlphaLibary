package de.alphahelix.alphalibary.minigame.events.status;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.minigame.status.GameStatus;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class GameStatusChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final GameStatus newGameState;
    private final GameStatus oldGameState;
    private boolean cancel;

    public GameStatusChangeEvent(GameStatus newGameState, GameStatus oldGameState) {
        this.newGameState = newGameState;
        this.oldGameState = oldGameState;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public GameStatus getNewGameState() {
        return newGameState;
    }

    public GameStatus getOldGameState() {
        return oldGameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatusChangeEvent that = (GameStatusChangeEvent) o;
        return Objects.equal(getNewGameState(), that.getNewGameState()) &&
                Objects.equal(getOldGameState(), that.getOldGameState());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNewGameState(), getOldGameState());
    }

    @Override
    public String toString() {
        return "GameStatusChangeEvent{" +
                "newGameState=" + newGameState +
                ", oldGameState=" + oldGameState +
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
