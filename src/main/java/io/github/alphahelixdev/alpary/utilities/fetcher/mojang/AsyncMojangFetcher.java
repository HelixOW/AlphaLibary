package io.github.alphahelixdev.alpary.utilities.fetcher.mojang;

import io.github.alphahelixdev.alpary.Alpary;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AsyncMojangFetcher implements MojangFetcher {

    private static final SimpleMojangFetcher SIMPLE_FETCHER = new SimpleMojangFetcher();

    @Override
    public AsyncMojangFetcher updateAPIStatus(APIServer server) {
        async(() -> {
            try {
                SIMPLE_FETCHER.updateAPIStatus(server);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    @Override
    public PlayerUUID getUUIDAtTime(String name, long time, boolean cached) {
        return null;
    }

    @Override
    public List<PlayerName> getNameHistory(UUID uuid, boolean cached) {
        return null;
    }

    @Override
    public PlayerProfile getPlayerProfile(UUID uuid, boolean cached) {
        return null;
    }

    private void async(Runnable r) {
        new BukkitRunnable() {
            @Override
            public void run() {
                r.run();
            }
        }.runTaskAsynchronously(Alpary.getInstance());
    }
}
