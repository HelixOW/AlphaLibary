package de.alphahelix.alphalibary.annotations.item;

import org.bukkit.entity.EntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("ALL")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SpawnEgg {

    EntityType spawned();

}