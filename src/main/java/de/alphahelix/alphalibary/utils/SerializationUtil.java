/*
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
 */
package de.alphahelix.alphalibary.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerializationUtil<T> {

    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    public static String jsonToString(JsonObject object) {
        return gson.toJson(object);
    }

    public static JsonObject stringToJson(String json) {
        try {
            return (JsonObject) parser.parse(json);
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public JsonObject serialize(T toSerialize) {
        String base64 = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(out);
            bukkitOut.writeObject(toSerialize);
            bukkitOut.close();
            base64 = Base64Coder.encodeLines(out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JsonObject object = new JsonObject();
        object.addProperty("item-data", base64);
        return object;
    }

    public T deserialize(JsonObject object) {
        T result = null;
        String itemData = object.get("item-data").getAsString();
        ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decodeLines(itemData));
        try {
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(in);
            result = (T) bukkitIn.readObject();
            bukkitIn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
