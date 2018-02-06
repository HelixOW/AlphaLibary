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

    /**
     * Is used to retrieve the name of the command, if nothing is set the default is the method name
     *
     * @return name of the command
     */
    String name() default "";

    /**
     * Is used to retrieve possible aliases for the command
     *
     * @return possible aliases
     */
    String[] alias() default {};

    /**
     * Describes the help, which is displayed by /help
     *
     * @return the usage of the command in /help
     */
    String usage() default "";

    /**
     * Retrieves a description which is shown by /help
     *
     * @return a description for this command
     */
    String description() default "";

    /**
     * Defines how many arguments need to be parsed
     *
     * @return the minimum arguments to be parsed
     */
    int min() default 0;

    /**
     * Defines how many arguments can be parsed
     *
     * @return the maximum arguments to be parsed
     */
    int max() default -1;

    /**
     * Defines if the console sender can also use this command. </br>
     * Please don't use <code>Player</code> as first argument when this is false.
     *
     * @return
     */
    boolean onlyPlayers() default false;

    /**
     * Defines a String which is shown in front of every message which is send by this command
     *
     * @return a prefix for every message send by this command
     */
    String resultPrefix() default "";

    /**
     * Defines a <code>ErrorHandler</code> to be used to handle errors
     *
     * @return a ErrorHandler implementation
     */
    Class<? extends ErrorHandler> errorHandler() default ResultErrorHandler.class;

}
