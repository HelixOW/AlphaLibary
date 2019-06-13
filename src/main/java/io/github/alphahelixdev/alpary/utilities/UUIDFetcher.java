package io.github.alphahelixdev.alpary.utilities;

import com.mojang.util.UUIDTypeAdapter;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.whoisalphahelix.helix.Cache;
import io.github.whoisalphahelix.helix.FailConsumer;
import io.github.whoisalphahelix.sql.SQL;
import io.github.whoisalphahelix.sql.SQLTable;
import io.github.whoisalphahelix.sql.annotations.Column;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
@Deprecated
public class UUIDFetcher {

    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/NAMES";

    private final UUIDCache cache;

    public UUIDFetcher() {
        Alpary.getInstance().gsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
        this.cache = new UUIDCache();
        Alpary.getInstance().cacheHandler().addCache(this.cache);
    }

    public void getUUID(Player p, FailConsumer<UUID> callback) {
        this.getUUID(p.getName(), callback);
    }

    public void getUUID(String name, FailConsumer<UUID> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Alpary.getInstance(), () -> {
            UUID id = null;
            try {
                id = this.getUUID(name);
            } catch (UUIDNotFoundException e) {
                callback.failt("Unable to find UUID for " + name);
            }

            if (id == null)
                callback.failt("Unable to find UUID for " + name);
            else
                callback.accept(id);
        });
    }

    public UUID getUUID(String name) throws UUIDNotFoundException {
        if (name == null)
            return null;

        name = name.toLowerCase();
        UUID cachedID = getCache().getUUID(name);

        if (cachedID != null)
            return cachedID;

        String finalName = name;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(this.getUUIDUrl(), finalName, System.currentTimeMillis() / 1000)).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID player = Alpary.getInstance().gson().fromJson(
                    new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID.class);

            if (player == null)
                return null;

            if (player.getUuid() == null)
                return null;


            this.getCache().getUuids().add(player);

            return player.getUuid();
        } catch (Exception e) {
            throw new UUIDNotFoundException(name);
        }
    }

    public String getUUIDUrl() {
        return UUIDFetcher.UUID_URL;
    }

    public void getUUID(OfflinePlayer p, FailConsumer<UUID> callback) {
        this.getUUID(p.getName(), callback);
    }

    public void getName(UUID uuid, FailConsumer<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Alpary.getInstance(), () -> {
            String name = "";
            try {
                name = getName(uuid);
            } catch (NameNotFoundException e) {
                callback.failt("Unable to find a skinName for " + uuid);
            }

            if (name.equals(""))
                callback.failt("Unable to find a skinName for " + uuid);
            else
                callback.accept(name);
        });
    }

    public String getName(UUID uuid) throws NameNotFoundException {
        if (uuid == null)
            return "";

        String cachedName = getCache().getName(uuid);

        if (!cachedName.isEmpty())
            return cachedName;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(this.getNameUrl(), UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID[] allUserNames = Alpary.getInstance().gson().fromJson(
                    new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID[].class);
            PlayerUUID currentName = allUserNames[allUserNames.length - 1];

            if (currentName == null)
                return "";

            if (currentName.getName() == null)
                return "";

            this.getCache().getUuids().add(currentName);

            return currentName.getName();
        } catch (Exception e) {
            throw new NameNotFoundException(uuid.toString());
        }
    }

    public String getNameUrl() {
        return UUIDFetcher.NAME_URL;
    }

    public UUID getUUID(Player p) throws UUIDNotFoundException {
        return this.getUUID(p.getName());
    }

    public UUID getUUID(OfflinePlayer p) throws UUIDNotFoundException {
        return this.getUUID(p.getName());
    }

    @Getter
    public class UUIDCache implements Cache {
        private final Set<PlayerUUID> uuids = new HashSet<>();

        private final SQLTable<PlayerUUID> playerSaveTable;

        public UUIDCache() {
            try {
                Alpary.getInstance().ioHandler().createFile(Alpary.getInstance().getDataFolder().getAbsolutePath() + "/uuidNameCache.db");
            } catch (IOException e) {
                e.printStackTrace();
            }

            SQL sql = new SQL("org.sqlite.JDBC",
                    "jdbc:sqlite:" + Alpary.getInstance().getDataFolder().getAbsolutePath() + "/uuidNameCache.db");
            this.playerSaveTable = sql.createTable(PlayerUUID.class,
                    (list) -> new PlayerUUID(list.get(0).toString(), UUID.fromString(list.get(1).toString())));

            this.uuids.addAll(this.playerSaveTable.getAll());
        }

        @Override
        public boolean clear() {
            this.getUuids().clear();

            return true;
        }

        @Override
        public String log() {
            return "UUID Cache cleared";
        }

        @Override
        public void save() {
            this.playerSaveTable.empty();

            this.uuids.forEach(this.playerSaveTable::insert);
        }

        public String getName(UUID id) {
            return uuids.stream().filter(playerUUID -> playerUUID.getUuid().equals(id)).map(PlayerUUID::getName).findFirst().orElse("");
        }

        public UUID getUUID(String name) {
            return uuids.stream().filter(playerUUID -> playerUUID.getName().equals(name)).map(PlayerUUID::getUuid).findFirst().orElse(null);
        }
    }

    public class UUIDNotFoundException extends Exception {
        UUIDNotFoundException(String name) {
            super("Unable to find UUID for " + name);
        }
    }

    public class NameNotFoundException extends Exception {
        NameNotFoundException(String uuid) {
            super("Unable to find Name for " + uuid);
        }
    }
}

@Getter
@AllArgsConstructor
@Table("player_ids")
class PlayerUUID {
    @Column(name = "name", type = "text")
    private String name;
    @Column(name = "uuid", type = "text")
    private UUID uuid;
}
