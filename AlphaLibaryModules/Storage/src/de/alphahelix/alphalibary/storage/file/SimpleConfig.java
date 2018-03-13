package de.alphahelix.alphalibary.storage.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.ReflectionHelper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class SimpleConfig extends SimpleJSONFile {
	
	static {
		JSONUtil.disableEscaping();
	}
	
	public SimpleConfig(String path, String name) {
		super(path, name);
	}
	
	public SimpleConfig(JavaPlugin plugin, String name) {
		super(plugin, name);
	}
	
	public static void initValues(String path, ConfigValue value) {
		initValues(path, value, "");
	}
	
	public static void initValues(String path, ConfigValue value, String replaced) {
		SimpleConfig valCfg = new SimpleConfig(path, value.getClass().getSimpleName().toLowerCase().replace(replaced, "") + ".json");
		
		valCfg.addValues(value);
		valCfg.applyValue(value);
	}
	
	public <T extends ConfigValue> void addValues(T value) {
		setDefaultValue(value.name(), value.save(value));
	}
	
	public <T extends ConfigValue> T applyValue(T defaultConfig) {
		JsonObject obj = getValue(defaultConfig.name(), JsonObject.class);
		
		for(Map.Entry<String, JsonElement> key : obj.entrySet()) {
			ReflectionHelper.SaveField sf = ReflectionHelper.getDeclaredField(key.getKey(), defaultConfig.getClass());
			
			sf.removeFinal();
			
			Object ob = JSONUtil.getGson().fromJson(key.getValue(), sf.field().getType());
			
			if(ob instanceof String)
				ob = ChatColor.translateAlternateColorCodes('&', (String) ob);
			
			sf.set(defaultConfig, ob, true);
		}
		
		return defaultConfig;
	}
	
	public static void initValues(JavaPlugin plugin, ConfigValue value) {
		initValues(plugin, value, "");
	}
	
	public static void initValues(JavaPlugin plugin, ConfigValue value, String replaced) {
		initValues(plugin.getDataFolder().getAbsolutePath(), value, replaced);
	}
}
