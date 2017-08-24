package de.alphahelix.alphalibary.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Accessor {

    public static Field access(Field f) throws ReflectiveOperationException {
        f.setAccessible(true);
        Field mods = Field.class.getDeclaredField("modifiers");
        mods.setAccessible(true);
        mods.setInt(f, f.getModifiers() & 0xFFFFFFEF);
        return f;
    }

    public static Method access(Method m) throws ReflectiveOperationException {
        m.setAccessible(true);
        return m;
    }
}
