package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a parameter to be completed automatically
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Complete {
	
	/**
	 * Defines the name of the command, where auto completion should be available
	 *
	 * @return the name of the command
	 */
	String name() default "";
	
}
