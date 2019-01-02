package io.github.alphahelixdev.alpary.annotations.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ItemColor {
    int red() default 0;

    int green() default 0;

    int blue() default 0;

}
