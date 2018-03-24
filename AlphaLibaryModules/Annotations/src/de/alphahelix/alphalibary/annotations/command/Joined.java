package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define a parameter to be joined in afterwards
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Joined {
	
	/**
	 * Can only be used once at the end of the parameters </br>
	 * Is used to create a String out of the left over arguments
	 *
	 * @return the newly created String
	 */
	String joinIn() default " ";
	
}
