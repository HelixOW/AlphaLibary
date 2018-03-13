package de.alphahelix.alphalibary.storage.file2.yaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigSection {
	
	private final ConfigSection parent;
	private final String key;
	private Object value;
	
	public ConfigSection(String key, Object value) {
		this(null, key, value);
	}
	
	public ConfigSection(ConfigSection parent, String key, Object value) {
		this.parent = parent;
		this.key = key;
		this.value = value;
	}
	
	public ConfigSection getParent() {
		return parent;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public ConfigSection getRoot() {
		if(isRoot())
			return this;
		return this.parent.getRoot();
	}
	
	public String getPath() {
		if(isRoot())
			return "";
		
		if(this.parent.isRoot())
			return this.key;
		
		StringBuilder path = new StringBuilder();
		
		if(!isRoot())
			path.append(this.parent.getPath()).append(".");
		
		path.append(this.key);
		
		return path.toString();
	}
	
	public String getKey() {
		return key;
	}
	
	public boolean contains(String path) {
		return get(path) != null;
	}
	
	public Object get() {
		return get("");
	}
	
	public Object get(String path) {
		return get(path, null);
	}
	
	public Object get(String path, Object def) {
		if(path == null)
			return def;
		
		path = path.trim();
		
		if(path.equals(""))
			return this.value;
		
		final ConfigSection section = getConfigSection(path);
		
		if(section == null)
			return def;
		
		return section.get("");
	}
	
	public String getString(String path) {
		return getString(path, "");
	}
	
	public String getString(String path, String def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof String)
			return (String) value;
		return def;
	}
	
	public boolean isString() {
		return isString("");
	}
	
	public boolean isString(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof String);
	}
	
	public int getInt() {
		return getInt("");
	}
	
	public int getInt(String path) {
		return getInt(path, 0);
	}
	
	public int getInt(String path, int def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof Integer)
			return (int) value;
		return def;
	}
	
	public boolean isInt() {
		return isInt("");
	}
	
	public boolean isInt(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof Integer);
		
	}
	
	public boolean getBoolean() {
		return getBoolean("");
	}
	
	public boolean getBoolean(String path) {
		return getBoolean(path, false);
	}
	
	public boolean getBoolean(String path, boolean def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof Boolean)
			return (boolean) value;
		return def;
	}
	
	public boolean isBoolean() {
		return isBoolean("");
	}
	
	public boolean isBoolean(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof Boolean);
	}
	
	public double getDouble() {
		return getDouble("");
	}
	
	public double getDouble(String path) {
		return getDouble(path, 0);
	}
	
	public double getDouble(String path, double def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof Double)
			return (double) value;
		return def;
	}
	
	public boolean isDouble() {
		return isDouble("");
	}
	
	public boolean isDouble(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof Double);
	}
	
	public float getFloat() {
		return getFloat("");
	}
	
	public float getFloat(String path) {
		return getFloat(path, 0);
	}
	
	public float getFloat(String path, float def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof Float)
			return (float) value;
		return def;
	}
	
	public boolean isFloat() {
		return isFloat("");
	}
	
	public boolean isFloat(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof Float);
	}
	
	public long getLong() {
		return getLong("");
	}
	
	public long getLong(String path) {
		return getLong(path, 0);
	}
	
	public long getLong(String path, long def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof Long)
			return (long) value;
		return def;
	}
	
	public boolean isLong() {
		return isLong("");
	}
	
	public boolean isLong(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof Long);
	}
	
	public List<?> getList() {
		return getList("");
	}
	
	public List<?> getList(String path) {
		return getList(path, null);
	}
	
	public List<?> getList(String path, List<?> def) {
		final Object value = get(path);
		
		if(value == null)
			return def;
		
		if(value instanceof List)
			return (List<?>) value;
		return def;
	}
	
	public boolean isList() {
		return isList("");
	}
	
	public boolean isList(String path) {
		final Object value = get(path);
		
		return value != null && (value instanceof List);
		
	}
	
	public List<String> getKeys() {
		return getKeys("");
	}
	
	public List<String> getKeys(String path) {
		if(path == null)
			return new ArrayList<>();
		
		path = path.trim();
		
		if(!isConfigSection(path))
			return new ArrayList<>();
		
		final ConfigSection section = getConfigSection(path);
		
		if(!section.isHoldingConfigSections())
			return new ArrayList<>();
		
		@SuppressWarnings("unchecked") final List<ConfigSection> sections = (List<ConfigSection>) section.get("");
		final List<String> keys = new ArrayList<>();
		
		for(ConfigSection entry : sections)
			keys.add(entry.getKey());
		
		return keys;
	}
	
	public ConfigSection getSection(String path) {
		return getConfigSection(path);
	}
	
	public ConfigSection getConfigSection(String path) {
		if(path == null)
			return null;
		
		path = path.trim();
		
		if(path.equals(""))
			return this;
		
		if(!isSet(""))
			return null;
		
		if(!path.contains(".")) {
			if(!isConfigSection(path))
				return null;
			
			if(this.value instanceof List) {
				try {
					@SuppressWarnings("unchecked")
					List<ConfigSection> sections = (List<ConfigSection>) this.value;
					for(ConfigSection section : sections) {
						if(section == null)
							continue;
						
						if(section.getKey().equals(path))
							return section;
					}
				} catch(ClassCastException ignored) {
				}
				return null;
				
			} else
				return null;
			
		} else {
			String[] keys = path.split("\\.");
			String key = path;
			if(keys.length > 0)
				key = keys[0];
			StringBuilder subPath = new StringBuilder();
			if(keys.length > 1) {
				subPath = new StringBuilder(keys[1]);
				for(int i = 2; i < keys.length; i++)
					subPath.append(".").append(keys[i]);
			}
			
			if(key.equals(""))
				return this;
			
			ConfigSection section = getConfigSection(key);
			
			if(section == null)
				return null;
			
			return section.getConfigSection(subPath.toString());
		}
	}
	
	public ConfigSection createSection(String path) {
		return createConfigSection(path);
	}
	
	public ConfigSection createConfigSection(String path) {
		if(path == null)
			return null;
		
		path = path.trim();
		
		if(path.equals(""))
			return this;
		
		String[] keys = path.split("\\.");
		String key = path;
		if(keys.length > 0)
			key = keys[0];
		StringBuilder subPath = new StringBuilder();
		if(keys.length > 1) {
			subPath = new StringBuilder(keys[1]);
			for(int i = 2; i < keys.length; i++)
				subPath.append(".").append(keys[i]);
			subPath = new StringBuilder(subPath.toString().trim());
		}
		
		if(isConfigSection(key)) {
			final ConfigSection section = getConfigSection(key);
			
			if(subPath.length() == 0)
				return section;
			
			return section.createConfigSection(subPath.toString());
		} else {
			if(this.value instanceof List) {
				try {
					List<ConfigSection> sections = (List<ConfigSection>) this.value;
					ConfigSection section = new ConfigSection(this, key, null);
					sections.add(section);
					this.value = sections;
					
					if(subPath.length() == 0)
						return section;
					
					return section.createConfigSection(subPath.toString());
				} catch(ClassCastException ignored) {
				}
			}
			
			final ConfigSection section = new ConfigSection(this, key, null);
			List<ConfigSection> sections = new ArrayList<>();
			sections.add(section);
			this.value = sections;
			
			if(subPath.length() == 0)
				return section;
			
			return section.createConfigSection(subPath.toString());
		}
	}
	
	public void set(Object value) {
		set("", value);
	}
	
	public void set(String path, Object value) {
		if(path == null)
			return;
		
		path = path.trim();
		
		if(path.equals("")) {
			this.value = value;
			return;
		}
		
		String[] keys = path.split("\\.");
		String key = path;
		if(keys.length > 0)
			key = keys[0];
		StringBuilder subPath = new StringBuilder();
		if(keys.length > 1) {
			subPath = new StringBuilder(keys[1]);
			for(int i = 2; i < keys.length; i++)
				subPath.append(".").append(keys[i]);
		}
		
		if(isConfigSection(key)) {
			final ConfigSection section = getConfigSection(key);
			section.set(subPath.toString(), value);
			
		} else {
			final ConfigSection section = new ConfigSection(this, key, null);
			if(this.value instanceof List) {
				try {
					List<ConfigSection> sections = (List<ConfigSection>) this.value;
					sections.add(section);
					
				} catch(ClassCastException ex) {
					List<ConfigSection> sections = new ArrayList<>();
					sections.add(section);
					this.value = sections;
				}
			} else {
				List<ConfigSection> sections = new ArrayList<>();
				sections.add(section);
				this.value = sections;
			}
			
			section.set(subPath.toString(), value);
		}
	}
	
	public boolean isSet(String path) {
		if(path == null)
			return false;
		
		final ConfigSection section = getConfigSection(path);
		
		return section != null && section.get("") != null;
	}
	
	public boolean isHoldingConfigSections() {
		if(this.value == null)
			return false;
		
		try {
			final List<ConfigSection> sections = (List<ConfigSection>) this.value;
			return sections.size() > 0 && (sections.get(0) != null);
			
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	public boolean isSection() {
		return isSection("");
	}
	
	public boolean isSection(String path) {
		return isConfigSection(path);
	}
	
	public boolean isConfigSection() {
		return isConfigSection("");
	}
	
	public boolean isConfigSection(String path) {
		if(path == null)
			return false;
		
		path = path.trim();
		
		if(path.equals(""))
			return true;
		
		if(!(this.value instanceof List))
			return false;
		
		try {
			final List<ConfigSection> sections = (List<ConfigSection>) this.value;
			for(ConfigSection section : sections) {
				if(section == null)
					continue;
				
				String[] keys = path.split("\\.");
				String key = path;
				if(keys.length > 0)
					key = keys[0];
				StringBuilder subPath = new StringBuilder();
				if(keys.length > 1) {
					subPath = new StringBuilder(keys[1]);
					for(int i = 2; i < keys.length; i++)
						subPath.append(".").append(keys[i]);
				}
				
				if(!section.getKey().equals(key))
					continue;
				
				return section.isConfigSection(subPath.toString());
			}
			
			return false;
			
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	public Map<String, Object> getValues() {
		final Map<String, Object> out = new LinkedHashMap<>();
		
		if(this.key == null)
			return out;
		
		if(this.value instanceof List) {
			try {
				List<ConfigSection> sections = (List<ConfigSection>) this.value;
				for(ConfigSection entry : sections)
					if(entry.isHoldingConfigSections())
						out.put(entry.getKey(), entry.getValues());
					else
						out.put(entry.getKey(), entry.get(""));
				
			} catch(ClassCastException e) {
				out.put(getKey(), this.value);
			}
		} else
			out.put(getKey(), this.value);
		
		return out;
	}
}
