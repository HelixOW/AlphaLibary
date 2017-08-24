package de.alphahelix.alphalibary.annotations.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Item {


    /**
     * Need to be as follows:
     * <p>
     * NAME of Enchantment:level
     */
    String[] enchantments() default {};

    ItemFlag[] itemflags() default {};

    String name() default "";

    Material material() default Material.AIR;

    int amount() default 1;

    short damage() default 0;

    String[] lore() default {};

    boolean unbreakable() default false;

}
