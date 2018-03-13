package de.alphahelix.alphalibary.annotations.entity;

import de.alphahelix.alphalibary.annotations.IAnnotation;

import java.lang.reflect.Field;

public class EntityAnnotations implements IAnnotation {
	@Override
	public void load(Object obj) {
		Class<?> clazz = obj.getClass();
		
		for(Field field : clazz.getDeclaredFields()) {
			Entity entity = field.getAnnotation(Entity.class);
			
			if(entity == null) continue;
			
			AnnotatedEntity annotatedEntity = new AnnotatedEntity(obj, field, entity);
		}
	}
}
