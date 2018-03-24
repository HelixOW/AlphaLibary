package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.annotations.command.CommandAnnotations;
import de.alphahelix.alphalibary.annotations.entity.EntityAnnotations;
import de.alphahelix.alphalibary.annotations.item.ItemAnnotations;
import de.alphahelix.alphalibary.annotations.random.RandomAnnotations;

/**
 * Used to load all annotations
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class Annotations {
	
	public static final CommandAnnotations COMMAND = new CommandAnnotations();
	public static final ItemAnnotations ITEM = new ItemAnnotations();
	public static final RandomAnnotations RANDOM = new RandomAnnotations();
	public static final EntityAnnotations ENTITIES = new EntityAnnotations();
	
	public static final IAnnotation[] ANNOTATIONS = {COMMAND, ITEM, RANDOM, ENTITIES};
	
	public static void loadAll(Object clazz) {
		for(IAnnotation annotation : ANNOTATIONS)
			annotation.load(clazz);
	}
}
