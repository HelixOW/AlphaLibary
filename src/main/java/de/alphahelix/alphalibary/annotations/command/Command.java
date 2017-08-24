package de.alphahelix.alphalibary.annotations.command;

import de.alphahelix.alphalibary.annotations.command.errors.ErrorHandler;
import de.alphahelix.alphalibary.annotations.command.errors.ResultErrorHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    String name() default "";

    String[] alias() default {};

    String usage() default "";

    String description() default "";

    int min() default 0;

    int max() default -1;

    boolean onlyPlayers() default false;

    String resultPrefix() default "";

    Class<? extends ErrorHandler> errorHandler() default ResultErrorHandler.class;

}
