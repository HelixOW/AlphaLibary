package io.github.alphahelixdev.alpary.annotations.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Map {

    int[] color() default {};

    String locationName() default "";

    boolean scaling() default false;

}
