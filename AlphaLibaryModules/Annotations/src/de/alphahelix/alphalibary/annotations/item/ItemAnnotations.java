package de.alphahelix.alphalibary.annotations.item;

import de.alphahelix.alphalibary.annotations.IAnnotation;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ItemAnnotations implements IAnnotation {
	
	@Override
	public void load(Object clazz) {
		registerItems(clazz);
	}
	
	public Set<AnnotatedItem> registerItems(Object clazzObj) {
		Class<?> clazz = clazzObj.getClass();
		Set<AnnotatedItem> registeredItems = new HashSet<>();
		
		for(Field field : clazz.getDeclaredFields()) {
			Item item = field.getAnnotation(Item.class);
			
			if(item == null) continue;
			
			Skull skull = field.getAnnotation(Skull.class);
			Color color = field.getAnnotation(Color.class);
			Banner banner = field.getAnnotation(Banner.class);
			Map map = field.getAnnotation(Map.class);
			Potion potion = field.getAnnotation(Potion.class);
			SpawnEgg spawnEgg = field.getAnnotation(SpawnEgg.class);
			
			AnnotatedItem annotatedItem = new AnnotatedItem(clazzObj, field, item, skull, color, banner, map, potion, spawnEgg);
			registeredItems.add(annotatedItem.apply());
		}
		
		return registeredItems;
	}
}
