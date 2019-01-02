package io.github.alphahelixdev.alpary.annotations.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Item {

    Material material();

    int amount() default 1;

    ItemFlag[] itemflags() default {};

    String name() default "";

    int damage() default 0;

    String[] lore() default {};

    boolean unbreakable() default false;

}