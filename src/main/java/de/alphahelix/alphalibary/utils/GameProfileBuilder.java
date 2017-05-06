package de.alphahelix.alphalibary.utils;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.file.SimpleFile;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
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

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
    private static JSONParser parser = new JSONParser();

    private static HashMap<UUID, CachedProfile> cache = new HashMap<>();

    private static long cacheTime = -1;

    /**
     * Don't run in main thread!
     * <p>
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid The player uuid
     * @return The GameProfile
     * @throws IOException If something wents wrong while fetching
     * @see GameProfile
     */
    public static GameProfile fetch(UUID uuid) throws IOException {
        return fetch(uuid, false);
    }

    public static GameProfile changeSkin(String json, String url) {
        GameProfile result = gson.fromJson(json, GameProfile.class);

        JSONObject head;

        try {
            head = (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject sigNameVal = (JSONObject) ((JSONArray) head.get("properties")).get(0);
        PropertyMap properties = new PropertyMap();

                /*

                gameprofile as json ->

                {
                    "id":"8303e3c1289644aeb0f1f1618641c199",
                    "name":"AlphaHelix",
                    "properties":
                    [
                        {
                            "signature":"Mga+9iefNbT/Ha4KXOIrMl+UBa8OyjQV3NBU60uSK1wE+H/VPFuNdUszZPLAQo6sRWf9ECom9YDOmsUdphuo3DFrQTtmBFAGLv4eqM7oxSybwyIBm7s8QnRdQf2eYTF61ruTwBHRHnt2CGfCKtqvfNoC8/B+uAAe1JHpccKPs599PQ+WOdbdwnqHI1hKMXsrAEeAmfFKm2WA9ta6yGx+7h8YWS7SIZfRTpdKNkTrgAlTbnMjsZMKSIvU9Dp/0OTTT7KOHHKT15Pr7gNKZz5z+fJRCgr7yiHxApLdt53BnBgMoQ9I+RglpnebWN8LjAG3KZ/9KGl7frJkG23eOTLe4XnWl+4SoEvuptugyDGVS+2Mk4xOAMe4Fn5e50VOrV9kAR+HoUf/LrJDnEtRsl2H388JZaExd4rGCqm4xLMGBOs/xROFkGRL2PVfl4CVUQN/Wr3YY9dYiE4AQPA5nohGtXDL8odisGl9I/N09LKVx5lwntdwPD3JbU/mrWvwpPdIzcBAEltVzZ6IF8t4IlDl1D6qymLwdnhCIJ7v7uN+BGhuVGbg+phhLST3VzNEw6bS+0k9a+S4qX0aj/1Sk1oGrpTmYdeYuhjyzaENd0Su01NmwIA3GdmP4kVeh8HI5URRSMQfUz9nGtnCHOTumiHqZ17FI4Wj4i/3GaCGn5lI4XU=",
                            "name":"textures",
                            "value":"eyJ0aW1lc3RhbXAiOjE0OTM5MjQ2MDQxOTUsInByb2ZpbGVJZCI6IjgzMDNlM2MxMjg5NjQ0YWViMGYxZjE2MTg2NDFjMTk5IiwicHJvZmlsZU5hbWUiOiJBbHBoYUhlbGl4Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NDg3MTFjM2UzZTMyOWNjZmE2NTZkZGEzZWQ4NGE5ZTU2MmZkODY2YmQ0OTQxYWM1OWZiYzEzNjJkNmU0ZDViIn19fQ=="
                        }
                    ]
                 }


                  Value ->
                 {
                    "timestamp":1493924604195,
                    "profileId":"8303e3c1289644aeb0f1f1618641c199",
                    "profileName":"AlphaHelix",
                    "signatureRequired":true,
                    "textures":{
                        "SKIN":{
                            "url":"http://textures.minecraft.net/texture/ebe4f9d1bcb3855c0c76059c4837c3bc53db472ae192e2f10103018dc868e9"
                        }
                     }
                   }
                 */

                /*
                public PropertyMap deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
                    PropertyMap var4 = new PropertyMap();
                    if(var1 instanceof JsonObject) {
                        JsonObject var5 = (JsonObject)var1;
                        Iterator var6 = var5.entrySet().iterator();

                        while(true) {
                            Entry var7;
                            do {
                                if(!var6.hasNext()) {
                                    return var4;
                                }

                                var7 = (Entry)var6.next();
                            } while(!(var7.getValue() instanceof JsonArray));

                            Iterator var8 = ((JsonArray)var7.getValue()).iterator();

                            while(var8.hasNext()) {
                                JsonElement var9 = (JsonElement)var8.next();
                                var4.put(var7.getKey(), new Property((String)var7.getKey(), var9.getAsString()));
                            }
                        }
                    } else if(var1 instanceof JsonArray) {
                        Iterator var10 = ((JsonArray)var1).iterator();

                        while(var10.hasNext()) {
                            JsonElement var11 = (JsonElement)var10.next();
                            if(var11 instanceof JsonObject) {
                                JsonObject var12 = (JsonObject)var11;
                                String var13 = var12.getAsJsonPrimitive("name").getAsString();
                                String var14 = var12.getAsJsonPrimitive("value").getAsString();
                                if(var12.has("signature")) {
                                    var4.put(var13, new Property(var13, var14, var12.getAsJsonPrimitive("signature").getAsString()));
                                } else {
                                    var4.put(var13, new Property(var13, var14));
                                }
                            }
                        }
                    }

                return var4;
                }
                 */

        JSONObject headValue;

        try {
            headValue = (JSONObject) parser.parse(new String(Base64.decodeBase64((String) sigNameVal.get("value"))));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject textures = (JSONObject) headValue.get("textures");
        JSONObject SKIN = (JSONObject) textures.get("SKIN");

        SKIN.put("url", url);

        textures.put("SKIN", SKIN);

        headValue.put("textures", textures);

        String val = Base64.encodeBase64String(headValue.toJSONString().getBytes());

        // name, value, sig
        Property toMap = new Property("textures", val, (String) sigNameVal.get("signature"));

        properties.put("textures", toMap);

        try {
            Field propField = GameProfile.class.getDeclaredField("properties");
            propField.setAccessible(true);
            propField.set(result, properties);
            propField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Don't run in main thread!
     * <p>
     * Fetches the GameProfile from the Mojang servers
     *
     * @param uuid     The player uuid
     * @param forceNew If true the cache is ignored
     * @return The GameProfile
     * @throws IOException If something wents wrong while fetching
     * @see GameProfile
     */
    public static GameProfile fetch(UUID uuid, boolean forceNew) throws IOException {
        if (!forceNew && cache.containsKey(uuid) && cache.get(uuid).isValid()) {
            return cache.get(uuid).profile;
        } else if (AlphaLibary.getGameProfileFile().getProfile(uuid) != null)
            return AlphaLibary.getGameProfileFile().getProfile(uuid);
        else {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(SERVICE_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

                GameProfile result = gson.fromJson(json, GameProfile.class);
                cache.put(uuid, new CachedProfile(result));

                result = changeSkin(json, "http://textures.minecraft.net/texture/ebe4f9d1bcb3855c0c76059c4837c3bc53db472ae192e2f10103018dc868e9");

                AlphaLibary.getGameProfileFile().addProfile(json);

                return result;
            } else {
                if (!forceNew && cache.containsKey(uuid)) {
                    return cache.get(uuid).profile;
                }
                JsonObject error = (JsonObject) new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getErrorStream())).readLine());
                throw new IOException(error.get("error").getAsString() + ": " + error.get("errorMessage").getAsString());
            }
        }
    }

    /**
     * Builds a GameProfile for the specified args
     *
     * @param uuid The uuid
     * @param name The name
     * @param skin The url from the skin image
     * @return A GameProfile built from the arguments
     * @see GameProfile
     */
    public static GameProfile getProfile(UUID uuid, String name, String skin) {
        return getProfile(uuid, name, skin, null);
    }

    /**
     * Builds a GameProfile for the specified args
     *
     * @param uuid    The uuid
     * @param name    The name
     * @param skinUrl Url from the skin image
     * @param capeUrl Url from the cape image
     * @return A GameProfile built from the arguments
     * @see GameProfile
     */
    public static GameProfile getProfile(UUID uuid, String name, String skinUrl, String capeUrl) {
        GameProfile profile = new GameProfile(uuid, name);
        boolean cape = capeUrl != null && !capeUrl.isEmpty();

        List<Object> args = new ArrayList<>();
        args.add(System.currentTimeMillis());
        args.add(UUIDTypeAdapter.fromUUID(uuid));
        args.add(name);
        args.add(skinUrl);
        if (cape) args.add(capeUrl);

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

    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

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

    public static class GameProfileFile extends SimpleFile {
        public GameProfileFile() {
            super("plugins/AlphaLibary", "profiles.yml");
        }

        public void addProfile(String jsonProfile) {
            addArgumentsToList("Profiles", jsonProfile);
        }

        public GameProfile getProfile(UUID owner) {
            for (String jsonProfile : getStringList("Profiles")) {
                GameProfile result = gson.fromJson(jsonProfile, GameProfile.class);

                if (result.getId().equals(owner)) return result;
            }
            return null;
        }
    }
}