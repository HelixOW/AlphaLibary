package de.alphahelix.alphalibary.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AlphaHelix
 * @version 1.0
 * @see AlphaModule
 * @since 1.9.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Dependency {
	
	String[] dependencies();
	
}
