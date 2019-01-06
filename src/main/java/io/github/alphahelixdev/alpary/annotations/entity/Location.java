package io.github.alphahelixdev.alpary.annotations.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Location {
	
	double x();
	
	double y();
	
	double z();
	
	String world() default "world";
}
