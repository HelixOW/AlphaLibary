package de.alphahelix.alphalibary.annotations.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to declare the {@link Item} as a {@link org.bukkit.potion.Potion}
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Item
 * @see org.bukkit.potion.Potion
 * @since 1.9.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Potion {
	
	int[] duration() default {};
	
	int[] amplifier() default {};
	
	String[] type() default {};
	
}
