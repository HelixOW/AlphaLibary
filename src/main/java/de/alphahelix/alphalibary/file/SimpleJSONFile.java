/*
 *
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.file;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.alphahelix.alphalibary.utils.JSONUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

public class SimpleJSONFile extends File {

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

    public SimpleJSONFile(JavaPlugin pl, String child) {
        this(pl.getDataFolder().getAbsolutePath(), child);
    }


    public void removeValue(Object path) {
        if (!contains(path)) return;

        head.remove(path.toString());

        update();
    }

    public boolean contains(Object path) {
        try {
            return FileUtils.readFileToString(this, Charset.defaultCharset()).contains(path.toString());
        } catch (IOException e) {
            return false;
        }
    }

    private void update() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
            writer.write(JSONUtil.getGson().toJson(head));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void addValuesToList(Object path, T... value) {
        JsonArray array = new JsonArray();

        if (jsonContains(path))
            array = getValue(path, JsonArray.class);

        for (T obj : value)
            array.add(JSONUtil.getGson().toJsonTree(obj));

        setValue(path, array);
    }

    public boolean jsonContains(Object path) {

        JsonObject obj = read();

        if (obj == null) return false;

        return obj.get(path.toString()) != null;
    }

    public <T> T getValue(Object path, Class<T> definy) {
        JsonObject obj = read();

        if (obj == null) return null;

        return JSONUtil.getGson().fromJson(obj.get(path.toString()), definy);
    }

    public void setDefaultValue(Object path, Object value) {
        if (jsonContains(path)) return;

        setValue(path, value);
    }

    public void setValue(Object path, Object value) {
        setDefault(path, value);
    }

    private JsonObject read() {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return null;

            return JSONUtil.getGson().fromJson(file, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDefault(Object path, Object value) {
        head.add(path.toString(), JSONUtil.getGson().toJsonTree(value));

        update();
    }

    public <T> T[] getListValues(Object path, Class<T[]> definy) {
        return getValue(path, definy);
    }

    public <T> T getValue(Object path, TypeToken<T> token) {
        JsonObject obj = read();

        if (obj == null) return null;

        return JSONUtil.getGson().fromJson(obj.get(path.toString()), token.getType());
    }

    public <T> ArrayList<T> getValues(Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this, Charset.defaultCharset());

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();

            JsonObject obj = JSONUtil.getGson().fromJson(file, JsonObject.class);
            ArrayList<T> list = new ArrayList<>();

            for (Map.Entry<String, JsonElement> o : obj.entrySet()) {
                list.add(JSONUtil.getGson().fromJson(o.getValue(), definy));
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getPaths() {
        JsonObject obj = read();

        if (obj == null) return new ArrayList<>();

        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry<String, JsonElement> o : obj.entrySet()) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
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