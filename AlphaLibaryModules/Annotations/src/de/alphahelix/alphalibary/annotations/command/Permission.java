package de.alphahelix.alphalibary.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {

    /**
     * Defines a permission for a command </br>
     * A <code>Command</code> annotation has to be infront of it
     *
     * @return the permission
     */
    String value();

    /**
     * Defines a message which is send, if the player doesn't have the permission to execute the command
     *
     * @return the message
     */
    String permissionMsg() default "";

}
