package de.alphahelix.alphalibary.events.status;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.status.GameStatus;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStatusChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameStatus newGameState;
    private GameStatus oldGameState;

    public GameStatusChangeEvent(GameStatus newGameState, GameStatus oldGameState) {
        this.newGameState = newGameState;
        this.oldGameState = oldGameState;
    }

    public final static HandlerList getHandlerList() {
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
}
