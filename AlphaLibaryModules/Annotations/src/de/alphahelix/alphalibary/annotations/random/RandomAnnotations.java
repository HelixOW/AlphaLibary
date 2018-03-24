package de.alphahelix.alphalibary.annotations.random;

import de.alphahelix.alphalibary.annotations.IAnnotation;
import de.alphahelix.alphalibary.annotations.item.Item;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to register the randoms
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Item
 * @since 1.9.2.1
 */
public class RandomAnnotations implements IAnnotation {
	
	@Override
	public void load(Object clazz) {
		registerRandoms(clazz);
	}
	
	public Set<AnnotatedRandom> registerRandoms(Object clazzObj) {
		Class<?> clazz = clazzObj.getClass();
		Set<AnnotatedRandom> registeredRandoms = new HashSet<>();
		
		for(Field field : clazz.getDeclaredFields()) {
			BooleanRandom booleanRandom = field.getAnnotation(BooleanRandom.class);
			DoubleRandom doubleRandom = field.getAnnotation(DoubleRandom.class);
			IntegerRandom integerRandom = field.getAnnotation(IntegerRandom.class);
			StringRandom stringRandom = field.getAnnotation(StringRandom.class);
			UUIDRandom uuidRandom = field.getAnnotation(UUIDRandom.class);
			
			AnnotatedRandom annotatedRandom;
			
			if(booleanRandom != null)
				annotatedRandom = new AnnotatedRandom(clazzObj, field, booleanRandom);
			else if(doubleRandom != null)
				annotatedRandom = new AnnotatedRandom(clazzObj, field, doubleRandom);
			else if(integerRandom != null)
				annotatedRandom = new AnnotatedRandom(clazzObj, field, integerRandom);
			else if(stringRandom != null)
				annotatedRandom = new AnnotatedRandom(clazzObj, field, stringRandom);
			else if(uuidRandom != null)
				annotatedRandom = new AnnotatedRandom(clazzObj, field, uuidRandom);
			else
				continue;
			
			registeredRandoms.add(annotatedRandom.apply());
		}
		
		return registeredRandoms;
	}
}
