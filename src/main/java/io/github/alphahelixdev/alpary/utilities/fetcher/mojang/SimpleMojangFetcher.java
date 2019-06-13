package io.github.alphahelixdev.alpary.utilities.fetcher.mojang;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.util.UUIDTypeAdapter;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.whoisalphahelix.helix.Cache;
import io.github.whoisalphahelix.sql.SQL;
import io.github.whoisalphahelix.sql.SQLTable;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public final class SimpleMojangFetcher implements MojangFetcher {

    private final MojangCache cache = new MojangCache();

    public SimpleMojangFetcher() {
        Alpary.getInstance().cacheHandler().addCache(this.cache);
    }

    public SimpleMojangFetcher updateAPIStatus(MojangFetcher.APIServer server) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_STATUS_URL).openConnection();

        connection.setReadTimeout(5000);

        JsonElement response = JSON_PARSER.parse(new BufferedReader(
                new InputStreamReader(connection.getInputStream())));

        if (!response.isJsonArray())
            throw new IOException("Response from '" + API_STATUS_URL + "' was not a JSON Array");

        String colorResponse = ((JsonArray) response).get(server.ordinal()).getAsJsonObject().get(server.getServerName()).getAsString();

        if (colorResponse == null || colorResponse.isEmpty())
            throw new IOException("Color of '" + server.getServerName() + "' is not set");

        switch (colorResponse) {
            case "green":
                server.setStatus(MojangFetcher.APIStatus.NO_ISSUES);
                break;
            case "yellow":
                server.setStatus(MojangFetcher.APIStatus.SOME_ISSUES);
                break;
            default:
                server.setStatus(MojangFetcher.APIStatus.UNAVAILABLE);
                break;
        }

        return this;
    }

    public MojangFetcher.PlayerUUID getUUIDAtTime(String name, long time, boolean cached) throws IOException {
        if (cached) {
            Optional<MojangFetcher.PlayerUUID> opt = cache.uuids.stream()
                    .filter(playerUUID -> playerUUID.getTimeStamp() == time && playerUUID.getName().equals(name))
                    .findFirst();

            if (opt.isPresent())
                return opt.get();
        }

        HttpURLConnection connection =
                (HttpURLConnection) new URL(String.format(UUID_AT_TIME_URL, name, time)).openConnection();

        connection.setReadTimeout(5000);

        JsonElement response = JSON_PARSER.parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));

        if (!response.isJsonObject())
            throw new IOException("Response from " + String.format(UUID_AT_TIME_URL, name, time) + " was not a JSON Object");

        MojangFetcher.PlayerUUID uuid = GSON.fromJson(response, MojangFetcher.PlayerUUID.class);

        uuid.setTimeStamp(time);

        cache.uuids.add(uuid);

        return uuid;
    }

    public List<MojangFetcher.PlayerName> getNameHistory(UUID uuid, boolean cached) throws IOException {
        if (cached && cache.nameHistories.containsKey(uuid))
            return cache.nameHistories.get(uuid);

        HttpURLConnection connection =
                (HttpURLConnection) new URL(
                        String.format(NAME_HISTORY_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();

        connection.setReadTimeout(5000);

        JsonElement response = JSON_PARSER.parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));

        if (!response.isJsonArray())
            throw new IOException("Response from " + String.format(NAME_HISTORY_URL, uuid) + " was not a JSON Array");

        List<MojangFetcher.PlayerName> historyNames = new LinkedList<>();

        for (int i = 0; i < ((JsonArray) response).size(); i++) {
            MojangFetcher.PlayerName name = GSON.fromJson(((JsonArray) response).get(i), MojangFetcher.PlayerName.class);

            name.setUuid(uuid);
            historyNames.add(name);
        }

        cache.nameHistories.put(uuid, historyNames);

        return historyNames.stream().sorted(Comparator.comparing(MojangFetcher.PlayerName::changedAt)).collect(Collectors.toList());
    }

    public MojangFetcher.PlayerProfile getPlayerProfile(UUID uuid, boolean cached) throws IOException {
        if (cached) {
            Optional<MojangFetcher.PlayerProfile> opt = cache.profiles.stream()
                    .filter(playerProfile -> playerProfile.getId().equals(uuid))
                    .findFirst();

            if (opt.isPresent())
                return opt.get();
        }

        HttpURLConnection connection =
                (HttpURLConnection) new URL(
                        String.format(PROFILE_SKIN_CAPE_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();

        connection.setReadTimeout(5000);

        JsonElement response = JSON_PARSER.parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));

        if (!response.isJsonObject())
            throw new IOException("Response from " + String.format(PROFILE_SKIN_CAPE_URL, uuid) + " was not a JSON Object");

        MojangFetcher.PlayerProfile prof = GSON.fromJson(response, MojangFetcher.PlayerProfile.class);

        cache.profiles.add(prof);

        return prof;
    }

    @Getter
    final class MojangCache implements Cache {
        private final Set<MojangFetcher.PlayerUUID> uuids = new LinkedHashSet<>();
        private final Set<MojangFetcher.PlayerName> names = new LinkedHashSet<>();
        private final Set<MojangFetcher.PlayerProfile> profiles = new LinkedHashSet<>();
        private final Map<UUID, List<MojangFetcher.PlayerName>> nameHistories = new HashMap<>();

        private final SQLTable<MojangFetcher.PlayerUUID> uuidTable;
        private final SQLTable<MojangFetcher.PlayerProfile> profileTable;
        private final SQLTable<MojangFetcher.PlayerName> nameTable;

        MojangCache() {
            try {
                Alpary.getInstance().ioHandler().createFile(
                        Alpary.getInstance().getDataFolder().getAbsolutePath() + "/mojangCache.db");
            } catch (IOException e) {
                e.printStackTrace();
            }

            SQL sql = new SQL("org.sqlite.JDBC",
                    "jdbc:sqlite:" + Alpary.getInstance().getDataFolder().getAbsolutePath() + "/mojangCache.db");

            this.uuidTable = sql.createTable(MojangFetcher.PlayerUUID.class, objects ->
                    new MojangFetcher.PlayerUUID(UUID.fromString(objects.get(0).toString()),
                            objects.get(1).toString(),
                            Long.parseLong(objects.get(2).toString())));

            this.profileTable = sql.createTable(MojangFetcher.PlayerProfile.class, objects -> new MojangFetcher.PlayerProfile(
                    UUID.fromString(objects.get(0).toString()),
                    objects.get(1).toString(),
                    Boolean.parseBoolean(objects.get(2).toString()),
                    (MojangFetcher.PlayerProfile.ProfileProperty[]) objects.get(3)
            ));
            this.nameTable = sql.createTable(MojangFetcher.PlayerName.class, objects -> new MojangFetcher.PlayerName(
                    objects.get(0).toString(),
                    Long.parseLong(objects.get(1).toString()),
                    UUID.fromString(objects.get(2).toString())
            ));

            retrieveFromDatabase();
        }

        @Override
        public boolean clear() {
            return true;
        }

        @Override
        public String log() {
            return "Mojang Cache updated!";
        }

        @Override
        public void save() {
            this.uuids.forEach(cachedID -> {
                if (uuidTable.contains("id", cachedID.getId())) {
                    MojangFetcher.PlayerUUID rowID = uuidTable.getRow("id", cachedID.getId());

                    if (!rowID.getName().equals(cachedID.getName()))
                        uuidTable.update("id", cachedID.getId(), "name", cachedID.getName());
                } else
                    uuidTable.insert(cachedID);
            });

            this.profiles.forEach(cProfile -> {
                if (profileTable.contains("id", cProfile.getId())) {
                    MojangFetcher.PlayerProfile rowProfile = profileTable.getRow("id", cProfile.getId());

                    if (!Arrays.equals(rowProfile.getProps(), cProfile.getProps()))
                        profileTable.update("id", cProfile.getId(), "props", cProfile.getProps());
                } else
                    profileTable.insert(cProfile);
            });

            this.names.forEach(cName -> {
                if (nameTable.contains("name", cName.getName())) {
                    MojangFetcher.PlayerName rowName = nameTable.getRow("name", cName.getName());

                    if (!rowName.getUuid().equals(cName.getUuid()))
                        nameTable.update("name", cName.getName(), "id", cName.getUuid());
                } else
                    nameTable.insert(cName);
            });

            this.nameHistories.forEach((uuid, cNames) -> cNames.forEach(cName -> {
                if (nameTable.contains("name", cName.getName())) {
                    MojangFetcher.PlayerName rowName = nameTable.getRow("name", cName.getName());

                    if (!rowName.getUuid().equals(cName.getUuid()))
                        nameTable.update("name", cName.getName(), "id", cName.getUuid());
                } else
                    nameTable.insert(cName);
            }));

            this.nameHistories.forEach((uuid, playerNames) -> playerNames.forEach(nameTable::insert));
        }

        private void retrieveFromDatabase() {
            uuids.addAll(this.uuidTable.getAll());
            profiles.addAll(this.profileTable.getAll());
            names.addAll(this.nameTable.getAll());
        }
    }
}
