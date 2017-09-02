package de.alphahelix.alphalibary.utils;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import de.alphahelix.alphalibary.item.ItemBuilder;
import de.alphahelix.alphalibary.schematics.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class JSONUtil {

    private static GsonBuilder builder = new GsonBuilder()
            .registerTypeHierarchyAdapter(Location.class, new LocationSerializer())
            .registerTypeHierarchyAdapter(GameProfile.class, new GameProfileBuilder.GameProfileSerializer())
            .registerTypeHierarchyAdapter(PropertyMap.class, new PropertyMap.Serializer())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeHierarchyAdapter(Schematic.LocationDiff.class, new LocationDiffSerializer());

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
                name = meta.getAsJsonPrimitive("displayName").getAsString();
            }

            if (meta.has("lore")) {
                lore = JSONUtil.getGson().fromJson(meta.getAsJsonArray("lore"), List.class);
            }

            flags = JSONUtil.getGson().fromJson(meta.getAsJsonArray("flags"), Set.class);

        }

        ItemBuilder builder = new ItemBuilder(type).setAmount(amount).setDamage(durability);

        if (name != null)
            builder.setName(name);

        if (lore != null)
            builder.setLore(lore.toArray(new String[lore.size()]));

        if (flags != null)
            for (String flag : flags)
                builder.addItemFlags(ItemFlag.valueOf(flag));

        JsonArray enchantments = object.getAsJsonArray("enchantments");

        for (int i = 0; i < enchantments.size(); i++) {
            builder.addEnchantment(
                    Enchantment.getByName(enchantments.get(i).getAsJsonObject().getAsJsonPrimitive("id").getAsString()),
                    enchantments.get(i).getAsJsonObject().getAsJsonPrimitive("lvl").getAsInt());
        }

        return builder.build();
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
                meta.addProperty("displayName", itemMeta.getDisplayName());

            if (itemMeta.hasLore())
                meta.add("lore", JSONUtil.getGson().toJsonTree(itemMeta.getLore()));

            meta.add("flags", JSONUtil.getGson().toJsonTree(itemMeta.getItemFlags()));

            result.add("meta", meta);
        }

        return result;
    }
}

class LocationDiffSerializer implements JsonSerializer<Schematic.LocationDiff>, JsonDeserializer<Schematic.LocationDiff> {

    @Override
    public Schematic.LocationDiff deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = (JsonObject) jsonElement;

        Material mat = Material.getMaterial(obj.getAsJsonPrimitive("type").getAsString());
        MaterialData data = JSONUtil.getGson().fromJson(obj.getAsJsonObject("data"), MaterialData.class);
        int x = obj.getAsJsonPrimitive("x").getAsInt();
        int y = obj.getAsJsonPrimitive("y").getAsInt();
        int z = obj.getAsJsonPrimitive("z").getAsInt();

        return new Schematic.LocationDiff() {
            @Override
            public Material getBlockType() {
                return mat;
            }

            @Override
            public MaterialData getBlockData() {
                return data;
            }

            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }

            @Override
            public int getZ() {
                return z;
            }
        };
    }

    @Override
    public JsonElement serialize(Schematic.LocationDiff locationDiff, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        obj.addProperty("type", locationDiff.getBlockType().name());
        obj.add("data", JSONUtil.getGson().toJsonTree(locationDiff.getBlockData()));
        obj.addProperty("x", locationDiff.getX());
        obj.addProperty("y", locationDiff.getY());
        obj.addProperty("z", locationDiff.getZ());

        return obj;
    }
}