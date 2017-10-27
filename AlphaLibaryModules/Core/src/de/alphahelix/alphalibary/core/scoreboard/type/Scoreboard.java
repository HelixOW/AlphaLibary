package de.alphahelix.alphalibary.core.scoreboard.type;

import org.bukkit.entity.Player;

public interface Scoreboard {

    org.bukkit.scoreboard.Scoreboard activate();

    void deactivate();

    boolean isActivated();

    ScoreboardHandler getHandler();

    Scoreboard setHandler(ScoreboardHandler handler);

    long getUpdateInterval();

    Scoreboard setUpdateInterval(long updateInterval);

    Player getHolder();

}
