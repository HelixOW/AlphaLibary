package io.github.alphahelixdev.alpary.annotations.entity;

import io.github.alphahelixdev.alpary.utilities.entity.EntityAge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Entity {
	
	Class<? extends org.bukkit.entity.Entity> typeClazz();
	
	String name() default "";
	
	double health() default 20;
	
	boolean moveable() default true;
	
	boolean pickUpItem() default true;
	
	boolean glowing() default false;
	
	boolean gravity() default true;
	
	boolean invincible() default false;
	
	boolean ageLock() default false;
	
	EntityAge age() default EntityAge.ADULT;
}
