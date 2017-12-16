package de.alphahelix.alphalibary.annotations.entity;

import de.alphahelix.alphalibary.core.utils.entity.EntityAge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Entity {

    /**
     * Defines the Entity which should be spawned
     *
     * @return the Entity class which is used to spawn the Entity
     */
    Class<? extends org.bukkit.entity.Entity> typeClazz() default org.bukkit.entity.Entity.class;

    /**
     * Sets the name of this entity, if none is specified, then there will no be name
     *
     * @return the name of this entity
     */
    String name() default "";

    /**
     * Defines the health for this entity </br>
     * 20 is the default health
     *
     * @return the health of the entity
     */
    double health() default 20;

    /**
     * Defines if this entity is able to move or not
     *
     * @return if this entity can move
     */
    boolean moveable() default true;

    /**
     * Defines if this entity is able to pick up items which lay on the ground
     *
     * @return if this entity can pick up items
     */
    boolean pickUpItem() default false;

    /**
     * Defines if this entity has a white outline around his texture, which is seen through walls
     *
     * @return if this entity has white outline
     */
    boolean glowing() default false;

    /**
     * Defines if this entity is affected by gravity
     *
     * @return if this entity is affected by gravity
     */
    boolean gravity() default true;

    /**
     * Defines if this entity is able to be damaged
     *
     * @return if this entity can take damage
     */
    boolean invincible() default false;

    /**
     * Defines if this entity can age
     *
     * @return if this entity can age
     */
    boolean ageLock() default false;

    /**
     * Defines the age of this entity
     *
     * @return the age of this entity
     */
    EntityAge age() default EntityAge.ADULT;

    /**
     * Defines the x cord of the spawn location
     *
     * @return the x cord of the spawn location
     */
    double x();

    /**
     * Defines the y cord of the spawn location
     *
     * @return the y cord of the spawn location
     */
    double y();

    /**
     * Defines the z cord of the spawn location
     *
     * @return the z cord of the spawn location
     */
    double z();

    /**
     * Defines the world where this entity should be spawned in
     *
     * @return the name of the world
     */
    String world();
}
