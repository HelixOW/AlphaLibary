package de.alphahelix.alphalibary.annotations.item;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Banner {
	DyeColor[] color();
	
	PatternType[] type();
}
