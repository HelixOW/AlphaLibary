package de.alphahelix.alphalibary.storage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class ReflectionHelper {
	
	public static SaveField[] findFieldsNotAnnotatedWith(Class<? extends Annotation> annotation, Collection<Class<?>> classes) {
		return findFieldsNotAnnotatedWith(annotation, classes.toArray(new Class[classes.size()]));
	}
	
	public static SaveField[] findFieldsNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveField> fields = new LinkedList<>();
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(!f.isAnnotationPresent(annotation)) {
					fields.add(new SaveField(f, i));
				}
				i++;
			}
			i = 0;
		}
		
		return fields.toArray(new SaveField[fields.size()]);
	}
	
	public static SaveField[] findFieldsAnnotatedWith(Class<? extends Annotation> annotation, Collection<Class<?>> classes) {
		return findFieldsAnnotatedWith(annotation, classes.toArray(new Class[classes.size()]));
	}
	
	public static SaveField[] findFieldsAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveField> fields = new LinkedList<>();
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(f.isAnnotationPresent(annotation)) {
					fields.add(new SaveField(f, i));
				}
				i++;
			}
			i = 0;
		}
		
		return fields.toArray(new SaveField[fields.size()]);
	}
	
	public static SaveField findFieldAtIndex(int index, Collection<Class<?>> classes) {
		return findFieldAtIndex(index, classes.toArray(new Class[classes.size()]));
	}
	
	public static SaveField findFieldAtIndex(int index, Class<?>... classes) {
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(i == index)
					return new SaveField(f, i);
				i++;
			}
		}
		
		return new SaveField();
	}
	
	public static SaveField getDeclaredField(String name, Class<?> clazz) {
		try {
			Field f = clazz.getDeclaredField(name);
			return new SaveField(f, -1);
		} catch(Exception e) {
			e.printStackTrace();
			return new SaveField();
		}
	}
	
	public static class SaveField {
		
		private Field f;
		private int index;
		
		public SaveField(Field f) {
			this(f, -1);
		}
		
		public SaveField(Field f, int index) {
			try {
				f.setAccessible(true);
				this.f = f;
			} catch(Exception e) {
				e.printStackTrace();
			}
			this.index = index;
		}
		
		SaveField() {
		}
		
		public SaveField removeFinal() {
			try {
				if(Modifier.isFinal(field().getModifiers()))
					field().setInt(field(), field().getModifiers() & ~Modifier.FINAL);
			} catch(IllegalAccessException e) {
				e.printStackTrace();
			}
			return this;
		}
		
		public Field field() {
			return f;
		}
		
		public Object get(Object instance) {
			if(f == null) return new Object();
			try {
				return f.get(instance);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return new Object();
		}
		
		public Object get(Object instance, boolean stackTrace) {
			if(f == null) return new Object();
			try {
				return f.get(instance);
			} catch(Exception e) {
				if(stackTrace) e.printStackTrace();
			}
			return new Object();
		}
		
		public void set(Object instance, Object value, boolean stackTrace) {
			if(f == null) return;
			try {
				f.set(instance, value);
			} catch(Exception e) {
				if(stackTrace) e.printStackTrace();
			}
		}
		
		public int index() {
			return index;
		}
	}
}
