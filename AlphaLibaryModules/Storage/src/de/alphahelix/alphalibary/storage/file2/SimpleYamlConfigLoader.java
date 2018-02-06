package de.alphahelix.alphalibary.storage.file2;

import de.alphahelix.alphalibary.storage.ReflectionHelper;
import de.alphahelix.alphalibary.storage.file.AbstractFile;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @see YamlConfigFile
 * @see YamlConfigValue
 */
public class SimpleYamlConfigLoader extends SimpleFile {
    public SimpleYamlConfigLoader(String path, String name) {
        super(path, name);
    }

    public SimpleYamlConfigLoader(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    public SimpleYamlConfigLoader(File parent, String child) {
        super(parent, child);
    }

    public SimpleYamlConfigLoader(URI uri) {
        super(uri);
    }

    public SimpleYamlConfigLoader(AbstractFile file) {
        super(file);
    }

    public static void initValues(String pathToFile, Object cfg) {
        if (!cfg.getClass().isAnnotationPresent(YamlConfigFile.class))
            return;

        YamlConfigFile ymlFile = cfg.getClass().getAnnotation(YamlConfigFile.class);

        SimpleYamlConfigLoader cfgLoader = new SimpleYamlConfigLoader(pathToFile, ymlFile.filename().equals("") ? cfg.getClass().getSimpleName().toLowerCase() + ".yml" : ymlFile.filename());
        Map<YamlConfigValue, Object> fields = getFields(cfg);

        for (YamlConfigValue path : fields.keySet()) {
            cfgLoader.setDefaultValue(path.path(), fields.get(path));
        }

        cfgLoader.apply(cfg, fields);
    }

    public static void initValues(JavaPlugin plugin, Object cfg) {
        initValues(plugin.getDataFolder().getAbsolutePath(), cfg);
    }

    private static Map<YamlConfigValue, Object> getFields(Object cfg) {
        Map<YamlConfigValue, Object> fields = new HashMap<>();
        for (ReflectionHelper.SaveField f : ReflectionHelper.findFieldsAnnotatedWith(YamlConfigValue.class, cfg.getClass())) {
            YamlConfigValue val = f.field().getAnnotation(YamlConfigValue.class);

            fields.put(val, (f.get(cfg) instanceof String) ? ChatColor.translateAlternateColorCodes('&', ((String) f.get(cfg))) : f.get(cfg));
        }
        return fields;
    }

    private void apply(Object cfg, Map<YamlConfigValue, Object> fields) {
        for (ReflectionHelper.SaveField f : ReflectionHelper.findFieldsAnnotatedWith(YamlConfigValue.class, cfg.getClass())) {
            f.set(cfg, getCfg().get(f.field().getAnnotation(YamlConfigValue.class).path()), false);
        }
    }
}
