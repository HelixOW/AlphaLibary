package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Optional {
	
	/**
	 * Defines a placeholder, if the argument is not entered
	 *
	 * @return a placeholder
	 */
	String define() default "";
	
}
