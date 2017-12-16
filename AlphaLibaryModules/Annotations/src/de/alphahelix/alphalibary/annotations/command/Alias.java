package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Alias {

    /**
     * First argument always has to be the expected entered (and parseable) type </br>
     * Followed by all aliases for the argument
     *
     * @return all acceptable aliases for a argument inside the command
     */
    String[] alias() default {};

}
