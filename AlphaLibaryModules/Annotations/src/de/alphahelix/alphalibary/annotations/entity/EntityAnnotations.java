package de.alphahelix.alphalibary.annotations.entity;

import de.alphahelix.alphalibary.annotations.IAnnotation;
import de.alphahelix.alphalibary.annotations.command.Command;

import java.lang.reflect.Field;

/**
 * Used to register the entities
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Command
 * @since 1.9.2.1
 */
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
