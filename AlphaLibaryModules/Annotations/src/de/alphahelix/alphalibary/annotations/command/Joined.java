package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


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
