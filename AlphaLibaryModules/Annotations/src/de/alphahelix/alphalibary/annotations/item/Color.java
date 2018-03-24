package de.alphahelix.alphalibary.annotations.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a color for the {@link Item}
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Item
 * @since 1.9.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Color {
	
	int[] rgbColor() default {0, 0, 0};
	
}
