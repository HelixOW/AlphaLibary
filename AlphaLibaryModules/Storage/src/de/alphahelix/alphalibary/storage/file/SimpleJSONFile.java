package de.alphahelix.alphalibary.storage.file;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.IDataStorage;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleJSONFile extends AbstractFile implements IDataStorage {
	
	private final JsonObject head = new JsonObject();
	
	public SimpleJSONFile(File parent, String child) {
		super(parent, child);
	}
	
	public SimpleJSONFile(URI uri) {
		super(uri);
	}
	
	public SimpleJSONFile(String parent, String child) {
		super(parent, child);
	}
	
	public SimpleJSONFile(JavaPlugin plugin, String child) {
		super(plugin, child);
	}
	
	public SimpleJSONFile(AbstractFile file) {
		super(file);
	}
	
	@SafeVarargs
	public final <T> void addValuesToList(Object path, T... value) {
		JsonArray array = new JsonArray();
		
		if(jsonContains(path))
			array = getValue(path, JsonArray.class);
		
		for(T obj : value)
			array.add(JSONUtil.getGson().toJsonTree(obj));
		
		setValue(path, array);
	}
	
	public boolean jsonContains(Object path) {
		JsonObject val = read();
		
		return val != null && val.get(path.toString()) != null;
		
	}
	
	public <T> T getValue(Object path, Class<T> definy) {
		JsonObject obj = read();
		
		if(obj == null) return null;
		
		return JSONUtil.getGson().fromJson(obj.get(path.toString()), definy);
	}
	
	@Override
	public void setValue(Object path, Object value) {
		setDefault(path, value);
	}
	
	@Override
	public void setDefaultValue(Object path, Object value) {
		if(!jsonContains(path))
			setValue(path, value);
	}
	
	@Override
	public void removeValue(Object path) {
		if(!contains(path)) return;
		
		head.remove(path.toString());
		
		update();
	}
	
	public boolean contains(Object path) {
		try {
			return FileUtils.readFileToString(this, Charset.defaultCharset()).contains(path.toString());
		} catch(IOException e) {
			return false;
		}
	}
	
	@Override
	public <T> void getValue(Object path, Class<T> definy, Consumer<T> callback) {
		callback.accept(getValue(path, definy));
	}
	
	@Override
	public void getKeys(Consumer<List<String>> callback) {
		callback.accept(getPaths());
	}
	
	@Override
	public <T> void getValues(Class<T> definy, Consumer<List<T>> callback) {
		callback.accept(getValues(definy));
	}
	
	@Override
	public void hasValue(Object path, Consumer<Boolean> callback) {
		callback.accept(jsonContains(path));
	}
	
	public <T> ArrayList<T> getValues(Class<T> definy) {
		try {
			String file = FileUtils.readFileToString(this, Charset.defaultCharset());
			
			if(file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
				return new ArrayList<>();
			
			JsonObject obj = JSONUtil.getGson().fromJson(file, JsonObject.class);
			ArrayList<T> list = new ArrayList<>();
			
			for(Map.Entry<String, JsonElement> o : obj.entrySet()) {
				list.add(JSONUtil.getGson().fromJson(o.getValue(), definy));
			}
			
			return list;
		} catch(IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public ArrayList<String> getPaths() {
		JsonObject obj = read();
		
		if(obj == null) return new ArrayList<>();
		
		ArrayList<String> list = new ArrayList<>();
		
		for(Map.Entry<String, JsonElement> o : obj.entrySet()) {
			list.add(o.getKey());
		}
		
		return list;
	}
	
	private JsonObject read() {
		try {
			String file = FileUtils.readFileToString(this, Charset.defaultCharset());
			
			if(file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
				return null;
			
			return JSONUtil.getGson().fromJson(file, JsonObject.class);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setDefault(Object path, Object value) {
		head.add(path.toString(), JSONUtil.getGson().toJsonTree(value));
		
		update();
	}
	
	private void update() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
			writer.write(JSONUtil.getGson().toJson(head));
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> T[] getListValues(Object path, Class<T[]> definy) {
		return getValue(path, definy);
	}
	
	public <T> T getValue(Object path, TypeToken<T> token) {
		JsonObject obj = read();
		
		if(obj == null) return null;
		
		return JSONUtil.getGson().fromJson(obj.get(path.toString()), token.getType());
	}
	
	public boolean isEmpty() {
		try {
			return FileUtils.readFileToString(this, Charset.defaultCharset()).isEmpty();
		} catch(IOException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		SimpleJSONFile that = (SimpleJSONFile) o;
		return Objects.equal(head, that.head);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), head);
	}
	
	@Override
	public String toString() {
		return "SimpleJSONFile{" +
				"head=" + head +
				'}';
	}
}
