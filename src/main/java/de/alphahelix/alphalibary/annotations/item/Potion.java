package de.alphahelix.alphalibary.annotations.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Potion {

    int[] duration() default {};

    int[] amplifier() default {};

    String[] type() default {};

}
