package de.alphahelix.alphalibary.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.JSONUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class SimpleConfig extends SimpleJSONFile {

    public SimpleConfig(String path, String name) {
        super(path, name);
    }

    public SimpleConfig(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    public void addValues(ConfigValue<?> value) {
        setDefaultValue(value.name(), value.save());
    }

    public <T> ConfigValue<T> applyValue(ConfigValue<T> defaultConfig) {
        JsonObject obj = getValue(defaultConfig.name(), JsonObject.class);

        for (Map.Entry<String, JsonElement> key : obj.entrySet()) {
            ReflectionUtil.SaveField sf = ReflectionUtil.getDeclaredField(key.getKey(), defaultConfig.type().getClass());

            sf.set(defaultConfig.type(), JSONUtil.getGson().fromJson(key.getValue(), sf.field().getType()), true);
        }

        return defaultConfig;
    }
}
