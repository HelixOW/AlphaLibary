package de.alphahelix.alphalibary.annotations.entity;

import de.alphahelix.alphalibary.annotations.IAnnotation;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class EntityAnnotations implements IAnnotation {
    @Override
    public void load(Object obj) {
        Class<?> clazz = obj.getClass();
        Set<AnnotatedEntity> registeredEntities = new HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            Entity entity = field.getAnnotation(Entity.class);

            if (entity == null) continue;

            AnnotatedEntity annotatedEntity = new AnnotatedEntity(obj, field, entity);

            registeredEntities.add(annotatedEntity.apply());
        }
    }
}
