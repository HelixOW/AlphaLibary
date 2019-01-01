package io.github.alphahelixdev.alpary.annotations.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

public @interface Item {

    Material material();

    int amount() default 1;

    ItemFlag[] itemflags() default {};

    String name() default "";

    short damage() default 0;

    String[] lore() default {};

    boolean unbreakable() default false;

}