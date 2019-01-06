package io.github.alphahelixdev.alpary.annotations.command;

import io.github.alphahelixdev.alpary.annotations.command.errorhandlers.ErrorHandler;
import io.github.alphahelixdev.alpary.annotations.command.errorhandlers.VoidErrorHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Command {
	
	String name() default "";
	
	String description() default "";
	
	String usage() default "";
	
	String[] alias() default {};
	
	boolean playersOnly() default false;
	
	Class<? extends ErrorHandler> errorHandler() default VoidErrorHandler.class;
	
}
