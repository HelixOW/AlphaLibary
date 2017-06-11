package de.alphahelix.alphalibary.command.arguments;

import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerArgument implements IArgument<OfflinePlayer> {
    @Override
    public boolean isCorrect(String arg) {
        return UUIDFetcher.getUUID(arg) != null;
    }

    @Override
    public OfflinePlayer get(String arg) {
        if (isCorrect(arg))
            return Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(arg));
        return null;
    }
}
