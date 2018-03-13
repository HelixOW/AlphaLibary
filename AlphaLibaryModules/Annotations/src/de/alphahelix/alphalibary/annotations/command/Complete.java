package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


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
