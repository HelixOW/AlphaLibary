package de.alphahelix.alphalibary.storage.file;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.ReflectionHelper;

public interface ConfigValue {

    String name();

    default <T extends ConfigValue> JsonObject save(T type) {
        JsonObject obj = new JsonObject();

        for (ReflectionHelper.SaveField f : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, type.getClass())) {
            if (f.field().getType().equals(String.class))
                f.set(type, f.get(type).toString().replace("§", "&"), true);
            obj.add(f.field().getName(), JSONUtil.getGson().toJsonTree(f.get(type)));
        }

        return obj;
    }
}