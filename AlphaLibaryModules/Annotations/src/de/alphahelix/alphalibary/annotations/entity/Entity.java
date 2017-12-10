package de.alphahelix.alphalibary.annotations.entity;

import de.alphahelix.alphalibary.core.utils.entity.EntityAge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Entity {

    Class<? extends org.bukkit.entity.Entity> typeClazz() default org.bukkit.entity.Entity.class;

    String name() default "";

    double health() default 20;

    boolean moveable() default true;

    boolean pickUpItem() default false;

    boolean glowing() default false;

    boolean gravity() default true;

    boolean invincible() default false;

    boolean ageLock() default false;

    EntityAge age() default EntityAge.ADULT;

    double x();

    double y();

    double z();

    String world();
}
