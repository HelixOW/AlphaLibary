package de.alphahelix.alphalibary.storage.file2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.ReflectionHelper;
import de.alphahelix.alphalibary.storage.file.SimpleJSONFile;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;
import java.util.Map;

public class SimpleConfigLoader extends SimpleJSONFile {

    public SimpleConfigLoader(File parent, String child) {
        super(parent, child);
    }

    public SimpleConfigLoader(URI uri) {
        super(uri);
    }

    public SimpleConfigLoader(String parent, String child) {
        super(parent, child);
    }

    public SimpleConfigLoader(JavaPlugin plugin, String child) {
        super(plugin, child);
    }

    public static <T> void initValues(String path, T value, String replaced) {
        SimpleConfigLoader cfgLoader = new SimpleConfigLoader(path, value.getClass().getSimpleName().toLowerCase().replace(replaced, "") + ".json");

        cfgLoader.addValue(value);
        cfgLoader.applyValue(value);
    }

    public static void initValues(JavaPlugin plugin, Object value, String replaced) {
        initValues(plugin.getDataFolder().getAbsolutePath(), value, replaced);
    }

    public static void initValues(String path, Object value) {
        initValues(path, value, "");
    }

    public static void initValues(JavaPlugin plugin, Object value) {
        initValues(plugin, value, "");
    }

    public <T> void addValue(T value) {
        ConfigValue val = value.getClass().getAnnotation(ConfigValue.class);

        if (val == null) return;

        setDefaultValue(val.name(), save(value));
    }

    public <T> T applyValue(T defaultConfig) {
        ConfigValue val = defaultConfig.getClass().getAnnotation(ConfigValue.class);

        if (val == null) return defaultConfig;

        JsonObject obj = getValue(val.name(), JsonObject.class);

        for (Map.Entry<String, JsonElement> key : obj.entrySet()) {
            ReflectionHelper.SaveField sf = ReflectionHelper.getDeclaredField(key.getKey(), defaultConfig.getClass()).removeFinal();

            Object ob = JSONUtil.getGson().fromJson(key.getValue(), sf.field().getType());

            if (ob instanceof String)
                ob = ChatColor.translateAlternateColorCodes('&', (String) ob);

            sf.set(defaultConfig, ob, true);
        }

        return defaultConfig;
    }

    private <T> JsonObject save(T type) {
        JsonObject obj = new JsonObject();

        for (ReflectionHelper.SaveField f : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, type.getClass())) {
            if (f.field().getType().equals(String.class))
                f.set(type, f.get(type).toString().replace("§", "&"), true);
            obj.add(f.field().getName(), JSONUtil.getGson().toJsonTree(f.get(type)));
        }

        return obj;
    }
}
