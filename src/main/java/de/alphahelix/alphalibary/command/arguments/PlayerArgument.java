package de.alphahelix.alphalibary.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument implements IArgument<Player> {
    @Override
    public boolean isCorrect(String arg) {
        return Bukkit.getPlayer(arg) != null;
    }

    @Override
    public Player get(String arg) {
        if (isCorrect(arg))
            return Bukkit.getPlayer(arg);
        return null;
    }
}
