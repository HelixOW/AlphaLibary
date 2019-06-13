package io.github.alphahelixdev.alpary.utilities.fetcher.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import io.github.whoisalphahelix.sql.annotations.Column;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public interface MojangFetcher {

    String API_STATUS_URL = "https://status.mojang.com/check";
    String UUID_AT_TIME_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    String NAME_HISTORY_URL = "https://api.mojang.com/user/profiles/%s/names";
    String PROFILE_SKIN_CAPE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    JsonParser JSON_PARSER = new JsonParser();

    default MojangFetcher updateAllAPIStatus() throws IOException {
        for (MojangFetcher.APIServer server : MojangFetcher.APIServer.values())
            updateAPIStatus(server);

        return this;
    }

    MojangFetcher updateAPIStatus(MojangFetcher.APIServer server) throws IOException;

    default MojangFetcher.PlayerUUID getUUID(OfflinePlayer p) throws IOException {
        return getUUIDAtTime(p, LocalDateTime.now());
    }

    default MojangFetcher.PlayerUUID getUUID(String name) throws IOException {
        return getUUIDAtTime(name, LocalDateTime.now());
    }

    default MojangFetcher.PlayerUUID getUUID(MojangFetcher.PlayerName name) throws IOException {
        return getUUIDAtTime(name, LocalDateTime.now());
    }

    default MojangFetcher.PlayerUUID getUUID(Player p, boolean cached) throws IOException {
        return getUUIDAtTime(p, LocalDateTime.now(), cached);
    }

    default MojangFetcher.PlayerUUID getUUID(String name, boolean cached) throws IOException {
        return getUUIDAtTime(name, LocalDateTime.now(), cached);
    }

    default MojangFetcher.PlayerUUID getUUID(MojangFetcher.PlayerName name, boolean cached) throws IOException {
        return getUUIDAtTime(name, LocalDateTime.now(), cached);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(OfflinePlayer p, long time) throws IOException {
        return getUUIDAtTime(p.getName(), time);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(OfflinePlayer p, LocalDateTime time) throws IOException {
        return getUUIDAtTime(p.getName(), time);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(MojangFetcher.PlayerName name, long time) throws IOException {
        return getUUIDAtTime(name.getName(), time, false);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(String name, long time) throws IOException {
        return getUUIDAtTime(name, time, false);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(String name, LocalDateTime time) throws IOException {
        return getUUIDAtTime(name, time.atZone(ZoneId.systemDefault()).toEpochSecond(), false);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(MojangFetcher.PlayerName name, LocalDateTime time) throws IOException {
        return getUUIDAtTime(name.getName(), time.atZone(ZoneId.systemDefault()).toEpochSecond(), false);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(OfflinePlayer p, long time, boolean cached) throws IOException {
        return getUUIDAtTime(p.getName(), time, cached);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(OfflinePlayer p, LocalDateTime time, boolean cached) throws IOException {
        return getUUIDAtTime(p.getName(), time, cached);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(MojangFetcher.PlayerName name, long time, boolean cached) throws IOException {
        return getUUIDAtTime(name.getName(), time, cached);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(MojangFetcher.PlayerName name, LocalDateTime time, boolean cached) throws IOException {
        return getUUIDAtTime(name.getName(), time.atZone(ZoneId.systemDefault()).toEpochSecond(), cached);
    }

    default MojangFetcher.PlayerUUID getUUIDAtTime(String name, LocalDateTime time, boolean cached) throws IOException {
        return getUUIDAtTime(name, time.atZone(ZoneId.systemDefault()).toEpochSecond(), cached);
    }

    MojangFetcher.PlayerUUID getUUIDAtTime(String name, long time, boolean cached) throws IOException;

    List<PlayerName> getNameHistory(UUID uuid, boolean cached) throws IOException;

    default List<MojangFetcher.PlayerName> getNameHistory(MojangFetcher.PlayerUUID uuid, boolean cached) throws IOException {
        return getNameHistory(uuid.getId(), cached);
    }

    default List<MojangFetcher.PlayerName> getNameHistory(OfflinePlayer p, boolean cached) throws IOException {
        return getNameHistory(getUUID(p), cached);
    }

    default List<MojangFetcher.PlayerName> getNameHistory(String name, boolean cached) throws IOException {
        return getNameHistory(getUUID(name), cached);
    }

    default List<MojangFetcher.PlayerName> getNameHistory(UUID uuid) throws IOException {
        return getNameHistory(uuid, false);
    }

    default List<MojangFetcher.PlayerName> getNameHistory(MojangFetcher.PlayerUUID uuid) throws IOException {
        return getNameHistory(uuid.getId(), false);
    }

    default List<MojangFetcher.PlayerName> getNameHistory(OfflinePlayer p) throws IOException {
        return getNameHistory(getUUID(p));
    }

    default List<MojangFetcher.PlayerName> getNameHistory(String name) throws IOException {
        return getNameHistory(getUUID(name));
    }

    default MojangFetcher.PlayerName getName(UUID uuid, boolean cached) throws IOException {
        List<MojangFetcher.PlayerName> list = getNameHistory(uuid, cached);
        return list.get(list.size() - 1);
    }

    default MojangFetcher.PlayerName getName(MojangFetcher.PlayerUUID uuid, boolean cached) throws IOException {
        return getName(uuid.getId(), cached);
    }

    default MojangFetcher.PlayerName getName(OfflinePlayer p, boolean cached) throws IOException {
        return getName(getUUID(p), cached);
    }

    default MojangFetcher.PlayerName getName(String name, boolean cached) throws IOException {
        return getName(getUUID(name), cached);
    }

    default MojangFetcher.PlayerName getName(MojangFetcher.PlayerUUID uuid) throws IOException {
        return getName(uuid.getId(), false);
    }

    default MojangFetcher.PlayerName getName(UUID uuid) throws IOException {
        return getName(uuid, false);
    }

    default MojangFetcher.PlayerName getName(OfflinePlayer p) throws IOException {
        return getName(getUUID(p));
    }

    default MojangFetcher.PlayerName getName(String name) throws IOException {
        return getName(getUUID(name));
    }

    MojangFetcher.PlayerProfile getPlayerProfile(UUID uuid, boolean cached) throws IOException;

    default MojangFetcher.PlayerProfile getPlayerProfile(MojangFetcher.PlayerUUID uuid, boolean cached) throws IOException {
        return getPlayerProfile(uuid.getId(), cached);
    }

    default MojangFetcher.PlayerProfile getPlayerProfile(OfflinePlayer p, boolean cached) throws IOException {
        return getPlayerProfile(getUUID(p), cached);
    }

    default MojangFetcher.PlayerProfile getPlayerProfile(String name, boolean cached) throws IOException {
        return getPlayerProfile(getUUID(name), cached);
    }

    default MojangFetcher.PlayerProfile getPlayerProfile(MojangFetcher.PlayerUUID uuid) throws IOException {
        return getPlayerProfile(uuid.getId(), false);
    }

    default MojangFetcher.PlayerProfile getPlayerProfile(UUID uuid) throws IOException {
        return getPlayerProfile(uuid, false);
    }

    default MojangFetcher.PlayerProfile getPlayerProfile(OfflinePlayer p) throws IOException {
        return getPlayerProfile(getUUID(p), false);
    }

    default MojangFetcher.PlayerProfile getPlayerProfile(String name) throws IOException {
        return getPlayerProfile(getUUID(name), false);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    enum APIServer {
        MINECRAFT_NET("minecraft.net", APIStatus.NO_ISSUES),
        SESSION_MINECRAFT_NET("session.minecraft.net", APIStatus.NO_ISSUES),
        ACCOUNT_MOJANG_COM("account.mojang.com", APIStatus.NO_ISSUES),
        AUTHSERVER_MOJANG_COM("authserver.mojang.com", APIStatus.NO_ISSUES),
        SESSIONSERVER_MOJANG_COM("sessionserver.mojang.com", APIStatus.NO_ISSUES),
        API_MOJANG_COM("api.mojang.com", APIStatus.NO_ISSUES),
        TEXTURES_MINECRAFT_NET("textures.minecraft.net", APIStatus.NO_ISSUES),
        MOJANG_COM("mojang.com", APIStatus.NO_ISSUES);

        private final String serverName;
        @Setter
        private APIStatus status;
    }

    enum APIStatus {
        NO_ISSUES, SOME_ISSUES, UNAVAILABLE
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Table("player_ids")
    final class PlayerUUID {
        @Column(type = "text")
        private final UUID id;
        @Column(type = "text")
        private final String name;
        @Setter
        @Column(type = "text")
        private long timeStamp;

        public LocalDateTime timeStampAsDate() {
            return Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Table("player_names")
    final class PlayerName {
        @Column(type = "text")
        private final String name;
        @Column(type = "int")
        private final long changedAt;
        @Column(type = "text")
        @Setter
        private UUID uuid;

        public LocalDateTime changedAt() {
            return Instant.ofEpochMilli(changedAt).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Table("player_profiles")
    final class PlayerProfile {
        @Column(type = "text")
        private final UUID id;
        @Column(type = "text")
        private final String name;
        @Column(type = "text")
        private boolean legacy;
        @Column(type = "text")
        private ProfileProperty[] props;

        @Getter
        @ToString
        @EqualsAndHashCode
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public final class ProfileProperty {
            private final String name;
            private final String value;
            private final String signature;
        }
    }

}
