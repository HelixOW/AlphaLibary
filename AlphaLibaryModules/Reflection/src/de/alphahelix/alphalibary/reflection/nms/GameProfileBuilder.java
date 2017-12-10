package de.alphahelix.alphalibary.reflection.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.core.utils.Util;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class GameProfileBuilder {

    private static final String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final String JSON_SKIN = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}";
    private static final String JSON_CAPE = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}";

    private static final Map<UUID, CachedProfile> CACHE = new HashMap<>();

    private static long cacheTime = -1;

    static {
        JSONUtil.disableEscaping();
    }

    /**
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid The player uuid
     * @see GameProfile
     */
    public static void fetch(UUID uuid, Consumer<GameProfile> callback) {
        fetch(uuid, false, callback);
    }

    /**
     * Don't run in main thread!
     * <p>
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid The player uuid
     * @see GameProfileBuilder#fetch(UUID, GameProfileCallback)
     */
    @Deprecated
    public static GameProfile fetch(UUID uuid) {
        return fetch(uuid, false);
    }

    /**
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid     The player uuid
     * @param forceNew If true the CACHE is ignored
     * @see GameProfile
     */
    public static void fetch(UUID uuid, boolean forceNew, Consumer<GameProfile> callback) {
        if (!forceNew && CACHE.containsKey(uuid) && CACHE.get(uuid).isValid())
            callback.accept(CACHE.get(uuid).profile);
        else Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            GameProfile prof = fetch(uuid, forceNew);

            System.out.println(callback == null);

            callback.accept(prof);
            // callback.accept(fetch(uuid, forceNew));
        });
    }

    /**
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid     The player uuid
     * @param forceNew If true the CACHE is ignored
     * @see GameProfileBuilder#fetch(UUID, boolean, GameProfileCallback)
     */
    public static GameProfile fetch(UUID uuid, boolean forceNew) {
        if (!forceNew && CACHE.containsKey(uuid)) {
            return CACHE.get(uuid).profile;
        } else {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) new URL(String.format(SERVICE_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            } catch (IOException ignored) {
            }

            if (connection == null) return new GameProfile(UUID.randomUUID(), Util.generateRandomString(15));

            connection.setReadTimeout(5000);

            try {
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

                    GameProfile result = JSONUtil.getGson().fromJson(json, GameProfile.class);

                    CACHE.put(uuid, new CachedProfile(result));

                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new GameProfile(UUID.randomUUID(), Util.generateRandomString(15));
        }
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
     * Sets the time as long as you want to keep the gameprofiles in CACHE (-1 = never remove it)
     *
     * @param time CACHE time (default = -1)
     */
    public static void setCacheTime(long time) {
        cacheTime = time;
    }

    private static class CachedProfile {

        private final long timestamp = System.currentTimeMillis();
        private final GameProfile profile;

        CachedProfile(GameProfile profile) {
            this.profile = profile;
        }

        public boolean isValid() {
            return cacheTime < 0 || (System.currentTimeMillis() - timestamp) < cacheTime;
        }
    }
}