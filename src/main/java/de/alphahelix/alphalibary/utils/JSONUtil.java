package de.alphahelix.alphalibary.utils;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import de.alphahelix.alphalibary.achievements.Achievement;
import de.alphahelix.alphalibary.file.SimpleJSONFile;
import de.alphahelix.alphalibary.item.InventoryItem;
import de.alphahelix.alphalibary.item.ItemBuilder;
import de.alphahelix.alphalibary.schematics.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class JSONUtil {

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(GameProfile.class, new GameProfileBuilder.GameProfileSerializer())
            .registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeAdapter(Achievement.class, new AchievementSerializer())
            .registerTypeAdapter(Schematic.LocationDiff.class, new LocationDiffSerializer())
            .registerTypeAdapter(Schematic.class, new SchematicSerializer())
            .create();

    public static <T> String toJson(T toConvert) {
        return gson.toJson(toConvert);
    }

    public static <T> T getValue(String json, Class<T> definy) {
        return gson.fromJson(json, definy);
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
                lore = SimpleJSONFile.gson.fromJson(meta.getAsJsonArray("lore"), List.class);
            }

            flags = SimpleJSONFile.gson.fromJson(meta.getAsJsonArray("flags"), Set.class);

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
                meta.add("lore", SimpleJSONFile.gson.toJsonTree(itemMeta.getLore()));

            meta.add("flags", SimpleJSONFile.gson.toJsonTree(itemMeta.getItemFlags()));

            result.add("meta", meta);
        }

        return result;
    }
}

class AchievementSerializer implements JsonSerializer<Achievement>, JsonDeserializer<Achievement> {
    @Override
    public Achievement deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = (JsonObject) json;

        return new Achievement() {
            @Override
            public String getName() {
                return obj.getAsJsonPrimitive("name").getAsString();
            }

            @Override
            public InventoryItem getIcon() {
                return SimpleJSONFile.gson.fromJson(obj.getAsJsonObject("icon"), InventoryItem.class);
            }

            @Override
            public List<String> getDescription() {
                return SimpleJSONFile.gson.fromJson(obj.getAsJsonArray("description"), List.class);
            }
        };
    }

    @Override
    public JsonElement serialize(Achievement achievement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", achievement.getName());
        obj.add("icon", SimpleJSONFile.gson.toJsonTree(achievement.getIcon()));
        obj.add("description", SimpleJSONFile.gson.toJsonTree(achievement.getDescription()));

        return obj;
    }
}

class SchematicSerializer implements JsonSerializer<Schematic>, JsonDeserializer<Schematic> {
    @Override
    public Schematic deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = (JsonObject) jsonElement;

        return new Schematic() {
            @Override
            public String getName() {
                return obj.getAsJsonPrimitive("name").getAsString();
            }

            @Override
            public List<LocationDiff> getBlocks() {
                return SimpleJSONFile.gson.fromJson(obj.getAsJsonArray("locationDiffs"), List.class);
            }
        };
    }

    @Override
    public JsonElement serialize(Schematic schematic, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", schematic.getName());
        obj.add("locationDiffs", SimpleJSONFile.gson.toJsonTree(schematic.getBlocks()));

        return obj;
    }
}

class LocationDiffSerializer implements JsonSerializer<Schematic.LocationDiff>, JsonDeserializer<Schematic.LocationDiff> {

    @Override
    public Schematic.LocationDiff deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = (JsonObject) jsonElement;

        Material mat = Material.getMaterial(obj.getAsJsonPrimitive("type").getAsString());
        byte data = obj.getAsJsonPrimitive("data").getAsByte();
        int x = obj.getAsJsonPrimitive("x").getAsInt();
        int y = obj.getAsJsonPrimitive("y").getAsInt();
        int z = obj.getAsJsonPrimitive("z").getAsInt();

        return new Schematic.LocationDiff() {
            @Override
            public Material getBlockType() {
                return mat;
            }

            @Override
            public byte getBlockData() {
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
        obj.addProperty("data", locationDiff.getBlockData());
        obj.addProperty("x", locationDiff.getX());
        obj.addProperty("y", locationDiff.getY());
        obj.addProperty("z", locationDiff.getZ());

        return obj;
    }
}
