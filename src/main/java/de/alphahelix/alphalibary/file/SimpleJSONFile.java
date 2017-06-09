package de.alphahelix.alphalibary.file;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import de.alphahelix.alphalibary.item.ItemBuilder;
import de.alphahelix.alphalibary.utils.GameProfileBuilder;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleJSONFile extends File {

    public static Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationSerializer()).registerTypeAdapter(GameProfile.class, new GameProfileBuilder.GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).registerTypeAdapter(ItemStack.class, new ItemStackSerializer()).create();
    private JsonObject head = new JsonObject();

    public SimpleJSONFile(String parent, String child) {
        super(parent, child);
        if (!this.exists() && !isDirectory()) {
            try {
                getParentFile().mkdirs();
                createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDefault(String path, Object value) {
        Object enteredValue = value;

        if (value instanceof String)
            enteredValue = ((String) value).replace("§", "&");

        head.add(path, gson.toJsonTree(enteredValue));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
            writer.write(gson.toJson(head));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValue(String path, Object value) {
        setDefault(path, value);
    }

    public void addValuesToList(String path, Object... value) {
        JsonArray array = new JsonArray();

        if (contains(path)) {
            array = getValue(path, JsonArray.class);
        }

        for (Object obj : value)
            array.add(gson.toJsonTree(obj));

        setValue(path, array);
    }

    public void removeValue(String path) {
        if (!contains(path)) return;

        head.remove(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
            writer.write(gson.toJson(head));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> ArrayList<T> getListValues(String path, Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();


            JsonObject obj = gson.fromJson(file, JsonObject.class);
            JsonArray array = obj.getAsJsonArray(path);
            ArrayList<T> typeList = new ArrayList<>();

            for (int i = 0; i < array.size(); i++) {
                typeList.add(gson.fromJson(array.get(i), definy));
            }

            return typeList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public <T> T getValue(String path, Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return null;

            JsonObject obj = gson.fromJson(file, JsonObject.class);

            return gson.fromJson(obj.get(path), definy);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T getValue(String path, TypeToken<T> token) {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return null;

            JsonObject obj = gson.fromJson(file, JsonObject.class);

            return gson.fromJson(obj.get(path), token.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> ArrayList<T> getValues(Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();

            JsonObject obj = gson.fromJson(file, JsonObject.class);
            ArrayList<T> list = new ArrayList<>();

            for (Map.Entry<String, JsonElement> o : obj.entrySet()) {
                list.add(gson.fromJson(o.getValue(), definy));
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getPaths() {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();

            JsonObject obj = gson.fromJson(file, JsonObject.class);
            ArrayList<String> list = new ArrayList<>();

            for (Map.Entry<String, JsonElement> o : obj.entrySet()) {
                list.add(o.getKey());
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean contains(String path) {
        try {
            return FileUtils.readFileToString(this, Charset.defaultCharset()).contains(path);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean jsonContains(String path) {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return false;

            JsonObject obj = gson.fromJson(file, JsonObject.class);

            return obj.entrySet().contains(path);
        } catch (Exception e) {
            return contains(path);
        }
    }

    public boolean isEmpty() {
        try {
            return FileUtils.readFileToString(this, Charset.defaultCharset()).isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
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