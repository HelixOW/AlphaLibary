package de.alphahelix.alphalibary.annotations.random;

import de.alphahelix.alphalibary.annotations.IAnnotation;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class RandomAnnotations implements IAnnotation {

    public Set<AnnotatedRandom> registerRandoms(Object clazzObj) {
        Class<?> clazz = clazzObj.getClass();
        Set<AnnotatedRandom> registeredRandoms = new HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            BooleanRandom booleanRandom = field.getAnnotation(BooleanRandom.class);
            DoubleRandom doubleRandom = field.getAnnotation(DoubleRandom.class);
            IntegerRandom integerRandom = field.getAnnotation(IntegerRandom.class);
            StringRandom stringRandom = field.getAnnotation(StringRandom.class);
            UUIDRandom uuidRandom = field.getAnnotation(UUIDRandom.class);

            AnnotatedRandom annotatedRandom;

            if (booleanRandom != null)
                annotatedRandom = new AnnotatedRandom(clazzObj, field, booleanRandom);
            else if (doubleRandom != null)
                annotatedRandom = new AnnotatedRandom(clazzObj, field, doubleRandom);
            else if (integerRandom != null)
                annotatedRandom = new AnnotatedRandom(clazzObj, field, integerRandom);
            else if (stringRandom != null)
                annotatedRandom = new AnnotatedRandom(clazzObj, field, stringRandom);
            else if (uuidRandom != null)
                annotatedRandom = new AnnotatedRandom(clazzObj, field, uuidRandom);
            else
                continue;

            registeredRandoms.add(annotatedRandom.apply());
        }

        return registeredRandoms;
    }

    @Override
    public void load(Object clazz) {
        registerRandoms(clazz);
    }
}
