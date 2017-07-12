/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.file;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.alphahelix.alphalibary.utils.JSONUtil;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

public class SimpleJSONFile extends File {

    public static Gson gson = JSONUtil.getGson();
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

    public void removeValue(String path) {
        if (!contains(path)) return;

        head.remove(path);
	
	    update();
    }
	
	public boolean contains (String path) {
		try {
			return FileUtils.readFileToString(this, Charset.defaultCharset()).contains(path);
		} catch(IOException e) {
			return false;
		}
	}
	
	private void update () {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
			writer.write(gson.toJson(head));
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
    }

    public <T> void addValuesToList(String path, T... value) {
        JsonArray array = new JsonArray();

        if (jsonContains(path))
            array = getValue(path, JsonArray.class);

        for (T obj : value)
            array.add(gson.toJsonTree(obj));

        setValue(path, array);
    }
	
	public boolean jsonContains (String path) {
		
		JsonObject obj = read();
		
		if(obj == null) return false;
		
		return obj.get(path) != null;
	}

    public <T> T getValue(String path, Class<T> definy) {
	    JsonObject obj = read();
	
	    if(obj == null) return null;
	
	    return gson.fromJson(obj.get(path), definy);
    }
	
	public void setValue (String path, Object value) {
		setDefault(path, value);
	}
	
	private JsonObject read () {
		try {
			String file = FileUtils.readFileToString(this, Charset.defaultCharset());
			
			if(file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
				return null;
			
			return gson.fromJson(file, JsonObject.class);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	public void setDefault (String path, Object value) {
		head.add(path, gson.toJsonTree(value));
		
		update();
	}
	
	public <T> T[] getListValues (String path, Class<T[]> definy) {
		return getValue(path, definy);
	}

    public <T> T getValue(String path, TypeToken<T> token) {
	    JsonObject obj = read();
	
	    if(obj == null) return null;
	
	    return gson.fromJson(obj.get(path), token.getType());
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
		JsonObject obj = read();
		
		if(obj == null) return new ArrayList<>();
		
		ArrayList<String> list = new ArrayList<>();
		
		for(Map.Entry<String, JsonElement> o : obj.entrySet()) {
			list.add(o.getKey());
		}
		
		return list;
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