package de.alphahelix.alphalibary.core.utils;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;
import java.util.*;

public class JSONUtil {

    private static final GsonBuilder builder = new GsonBuilder()
            .registerTypeHierarchyAdapter(Location.class, new LocationSerializer())
            .registerTypeHierarchyAdapter(GameProfile.class, new GameProfileSerializer())
            .registerTypeHierarchyAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeHierarchyAdapter(PropertyMap.class, new PropertyMap.Serializer())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackSerializer())
            ;

    private static Gson gson = builder.create();

    public static void addTypeAdapter(Class<?> clazz, Object adapter) {
        builder.registerTypeHierarchyAdapter(clazz, adapter);
        gson = builder.create();
    }

    public static String toJson(Object toConvert) {
        return gson.toJson(toConvert);
    }

    public static String toJson(Object obj, Class<?> type) {
        return gson.toJson(obj, type);
    }

    public static <T> T getValue(String json, Class<T> definy) {
        return gson.fromJson(json, definy);
    }

    public static <T> T getValue(JsonElement json, Class<?> definy) {
        return gson.fromJson(json, (Class<T>) definy);
    }

    public static void disableEscaping() {
        gson = builder.disableHtmlEscaping().create();
    }

    public static Gson getGson() {
        return gson;
    }
}

class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;

        String world = object.has("world") ? object.getAsJsonPrimitive("world").getAsString() : Bukkit.getWorlds().get(0).getName();
        double x = object.getAsJsonPrimitive("x").getAsDouble(), y = object.getAsJsonPrimitive("y").getAsDouble(), z = object.getAsJsonPrimitive("z").getAsDouble();
        float yaw = object.getAsJsonPrimitive("yaw").getAsFloat(), pitch = object.getAsJsonPrimitive("pitch").getAsFloat();

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        if (src.getWorld() != null)
            result.addProperty("world", src.getWorld().getName());

        result.addProperty("x", src.getX());
        result.addProperty("y", src.getY());
        result.addProperty("z", src.getZ());
        result.addProperty("yaw", src.getYaw());
        result.addProperty("pitch", src.getPitch());

        return result;
    }
}

class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;

        Material type = Material.getMaterial(object.getAsJsonPrimitive("type").getAsString());
        int amount = object.getAsJsonPrimitive("amount").getAsInt();
        short durability = object.getAsJsonPrimitive("durability").getAsShort();

        String name = null;
        List<String> lore = null;
        Set<String> flags = null;


        if (object.has("meta")) {
            JsonObject meta = object.getAsJsonObject("meta");

            if (meta.has("displayName")) {
                name = ChatColor.translateAlternateColorCodes('&', meta.getAsJsonPrimitive("displayName").getAsString());
            }

            if (meta.has("lore")) {
                lore = JSONUtil.getGson().<List<String>>fromJson(meta.getAsJsonArray("lore"), List.class);
            }

            flags = JSONUtil.getGson().<Set<String>>fromJson(meta.getAsJsonArray("flags"), Set.class);

        }

        ItemStack is = new ItemStack(type, amount, durability);
        ItemMeta meta = is.getItemMeta();

        if (name != null)
            meta.setDisplayName(name);

        if (lore != null) {
            List<String> colorLore = new ArrayList<>();
            for (String lor : lore)
                colorLore.add(ChatColor.translateAlternateColorCodes('&', lor));

            meta.setLore(colorLore);
        }

        if (flags != null)
            for (String flag : flags)
                meta.addItemFlags(ItemFlag.valueOf(flag));

        JsonArray enchantments = object.getAsJsonArray("enchantments");

        for (int i = 0; i < enchantments.size(); i++) {
            meta.addEnchant(
                    Enchantment.getByName(enchantments.get(i).getAsJsonObject().getAsJsonPrimitive("id").getAsString()),
                    enchantments.get(i).getAsJsonObject().getAsJsonPrimitive("lvl").getAsInt(), true);
        }

        is.setItemMeta(meta);

        return is;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonArray enchantments = new JsonArray();

        for (Enchantment e : src.getEnchantments().keySet()) {
            JsonObject ench = new JsonObject();

            ench.addProperty("id", e.getName());
            ench.addProperty("lvl", src.getEnchantments().get(e));

            enchantments.add(ench);
        }

        result.addProperty("type", src.getType().name());
        result.addProperty("amount", src.getAmount());
        result.addProperty("durability", src.getDurability());
        result.add("enchantments", enchantments);

        if (src.hasItemMeta()) {
            JsonObject meta = new JsonObject();
            ItemMeta itemMeta = src.getItemMeta();

            if (itemMeta.hasDisplayName())
                meta.addProperty("displayName", itemMeta.getDisplayName().replace("§", "&"));

            if (itemMeta.hasLore())
	            meta.add("lore", JSONUtil.getGson().toJsonTree(Arrays.asList(ArrayUtil.replaceInArray("§", "&", itemMeta.getLore().toArray(new String[itemMeta.getLore().size()])))));

            meta.add("flags", JSONUtil.getGson().toJsonTree(itemMeta.getItemFlags()));

            result.add("meta", meta);
        }

        return result;
    }
}

class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

    public GameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;
        UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
        String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
        GameProfile profile = new GameProfile(id, name);

        if (object.has("properties")) {
            for (Map.Entry<String, Property> prop : ((PropertyMap) context.deserialize(object.get("properties"), PropertyMap.class)).entries()) {
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

//class LocationDiffSerializer implements JsonSerializer<Schematic.LocationDiff>, JsonDeserializer<Schematic.LocationDiff> {
//
//    @Override
//    public Schematic.LocationDiff deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        JsonObject obj = (JsonObject) jsonElement;
//
//        Material mat = Material.getMaterial(obj.getAsJsonPrimitive("type").getAsString());
//        MaterialData data = JSONUtil.getGson().fromJson(obj.getAsJsonObject("data"), MaterialData.class);
//        int x = obj.getAsJsonPrimitive("x").getAsInt();
//        int y = obj.getAsJsonPrimitive("y").getAsInt();
//        int z = obj.getAsJsonPrimitive("z").getAsInt();
//
//        return new Schematic.LocationDiff() {
//            @Override
//            public Material getBlockType() {
//                return mat;
//            }
//
//            @Override
//            public MaterialData getBlockData() {
//                return data;
//            }
//
//            @Override
//            public int getX() {
//                return x;
//            }
//
//            @Override
//            public int getY() {
//                return y;
//            }
//
//            @Override
//            public int getZ() {
//                return z;
//            }
//        };
//    }
//
//    @Override
//    public JsonElement serialize(Schematic.LocationDiff locationDiff, Type type, JsonSerializationContext jsonSerializationContext) {
//        JsonObject obj = new JsonObject();
//
//        obj.addProperty("type", locationDiff.getBlockType().name());
//        obj.add("data", JSONUtil.getGson().toJsonTree(locationDiff.getBlockData()));
//        obj.addProperty("x", locationDiff.getX());
//        obj.addProperty("y", locationDiff.getY());
//        obj.addProperty("z", locationDiff.getZ());
//
//        return obj;
//    }
//}