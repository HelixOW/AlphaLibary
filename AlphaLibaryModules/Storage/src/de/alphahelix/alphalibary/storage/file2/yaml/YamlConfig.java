package de.alphahelix.alphalibary.storage.file2.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfig extends FileConfig {
	
	private static final int YAML_INDENT_SIZE = 4;
	private static final DumperOptions.FlowStyle YAML_FLOW_STYLE = DumperOptions.FlowStyle.BLOCK;
	private final DumperOptions options = new DumperOptions();
	private final Representer representer = new Representer();
	
	private final Yaml yaml = new Yaml(new Constructor(), representer, options);
	
	public YamlConfig() {
		options.setIndent(YAML_INDENT_SIZE);
		
		options.setDefaultFlowStyle(YAML_FLOW_STYLE);
		representer.setDefaultFlowStyle(YAML_FLOW_STYLE);
	}
	
	public static YamlConfig loadFromFile(String filePath) {
		final File file = new File(filePath);
		
		if(!file.isFile())
			return new YamlConfig();
		
		return loadFromFile(file);
	}
	
	public static YamlConfig loadFromFile(File file) {
		if(file == null)
			return new YamlConfig();
		
		final YamlConfig config = new YamlConfig();
		
		try {
			config.load(file);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	public static YamlConfig loadFromStream(InputStream stream) {
		if(stream == null)
			return new YamlConfig();
		
		final YamlConfig config = new YamlConfig();
		
		try {
			config.load(stream);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	public String saveToString() {
		return yaml.dump(getValues());
	}
	
	public void loadFromString(String config) {
		if(config == null)
			return;
		
		Map<?, ?> input = null;
		try {
			input = yaml.load(config);
		} catch(YAMLException | ClassCastException e) {
			e.printStackTrace();
		}
		
		if(input != null)
			convertMapsToSections(input, this);
	}
	
	private void convertMapsToSections(Map<?, ?> input, ConfigSection section) {
		for(Map.Entry<?, ?> entry : input.entrySet()) {
			final String key = entry.getKey().toString();
			final Object value = entry.getValue();
			
			if(value instanceof Map)
				convertMapsToSections((Map<?, ?>) value, section.createSection(key));
			else
				section.set(key, value);
		}
	}
}
