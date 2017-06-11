package de.alphahelix.alphalibary.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument extends Argument<Player> {
    @Override
    public boolean matches() {
        return Bukkit.getPlayer(getEnteredArgument()) != null;
    }

    @Override
    public Player fromArgument() {
        if (matches())
            return Bukkit.getPlayer(getEnteredArgument());
        return null;
    }
}
