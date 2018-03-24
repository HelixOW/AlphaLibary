package de.alphahelix.alphalibary.annotations.random;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to identify the field, which need a random int value
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntegerRandom {
	
	int length();
	
}
