package de.alphahelix.alphalibary.utils;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.file.SimpleJSONFile;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class GameProfileBuilder {

    private static final String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final String JSON_SKIN = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}";
    private static final String JSON_CAPE = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}";
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();

    private static HashMap<UUID, CachedProfile> cache = new HashMap<>();

    private static long cacheTime = -1;

    /**
     * Don't run in main thread!
     * <p>
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid The player uuid
     * @see GameProfile
     */
    public static void fetch(UUID uuid, GameProfileCallback callback) {
        fetch(uuid, false, callback);
    }

    /**
     * Don't run in main thread!
     * <p>
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid     The player uuid
     * @param forceNew If true the cache is ignored
     * @see GameProfile
     */
    public static void fetch(UUID uuid, boolean forceNew, GameProfileCallback callback) {
        if (!forceNew && cache.containsKey(uuid) && cache.get(uuid).isValid())
            callback.done(cache.get(uuid).profile);
        else if (AlphaLibary.getGameProfileFile().getProfile(uuid) != null)
            callback.done(AlphaLibary.getGameProfileFile().getProfile(uuid));
        else Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
                HttpURLConnection connection = null;

                try {
                    connection = (HttpURLConnection) new URL(String.format(SERVICE_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
                } catch (IOException ignored) {
                }

                if (connection == null) return;

                connection.setReadTimeout(5000);

                try {
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String json = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

                        GameProfile result = GSON.fromJson(json, GameProfile.class);
                        cache.put(uuid, new CachedProfile(result));

                        AlphaLibary.getGameProfileFile().addProfile(result);

                        callback.done(result);
                    } else if (!forceNew && cache.containsKey(uuid))
                        callback.done(cache.get(uuid).profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

    /**
     * Builds a GameProfile for the specified args
     *
     * @param name The name
     * @param skin The url from the skin image
     * @return A GameProfile built from the arguments
     * @see GameProfile
     */
    public static GameProfile getProfile(String name, String skin) {
        return getProfile(name, skin, null);
    }

    /**
     * Builds a GameProfile for the specified args
     *
     * @param name    The name
     * @param skinUrl Url from the skin image
     * @param capeUrl Url from the cape image
     * @return A GameProfile built from the arguments
     * @see GameProfile
     */
    public static GameProfile getProfile(String name, String skinUrl, String capeUrl) {
        UUID id = UUID.randomUUID();
        GameProfile profile = new GameProfile(id, name);
        boolean cape = capeUrl != null && !capeUrl.isEmpty();

        List<Object> args = new ArrayList<>();
        args.add(System.currentTimeMillis());
        args.add(UUIDTypeAdapter.fromUUID(id));
        args.add(name);
        args.add(skinUrl);
        if (cape) args.add(capeUrl);

        profile.getProperties().clear();
        profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(String.format(cape ? JSON_CAPE : JSON_SKIN, args.toArray(new Object[args.size()])))));
        return profile;
    }

    /**
     * Sets the time as long as you want to keep the gameprofiles in cache (-1 = never remove it)
     *
     * @param time cache time (default = -1)
     */
    public static void setCacheTime(long time) {
        cacheTime = time;
    }

    public static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

        public GameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = (JsonObject) json;
            UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
            String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
            GameProfile profile = new GameProfile(id, name);

            if (object.has("properties")) {
                for (Entry<String, Property> prop : ((PropertyMap) context.deserialize(object.get("properties"), PropertyMap.class)).entries()) {
                    profile.getProperties().put(prop.getKey(), prop.getValue());
                }
            }
            return profile;
        }

        public JsonElement serialize(GameProfile profile, Type type, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if (profile.getId() != null)
                result.add("id", context.serialize(profile.getId()));
            if (profile.getName() != null)
                result.addProperty("name", profile.getName());
            if (!profile.getProperties().isEmpty())
                result.add("properties", context.serialize(profile.getProperties()));
            return result;
        }

    }

    private static class CachedProfile {

        private long timestamp = System.currentTimeMillis();
        private GameProfile profile;

        public CachedProfile(GameProfile profile) {
            this.profile = profile;
        }

        public boolean isValid() {
            return cacheTime < 0 || (System.currentTimeMillis() - timestamp) < cacheTime;
        }
    }

    public static class GameProfileFile extends SimpleJSONFile {
        public GameProfileFile() {
            super("plugins/AlphaLibary", "profiles.json");
        }

        public void addProfile(GameProfile profile) {
            addValuesToList("Profiles", profile);
        }

        public GameProfile getProfile(UUID owner) {
            if (getListValues("Profiles", GameProfile[].class) != null)
                for (GameProfile profile : getListValues("Profiles", GameProfile[].class)) {
                    if (profile.getId().equals(owner)) return profile;
                }
            return null;
        }
    }
}