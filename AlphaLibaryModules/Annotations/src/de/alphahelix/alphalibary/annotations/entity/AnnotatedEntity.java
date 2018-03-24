package de.alphahelix.alphalibary.annotations.entity;

import de.alphahelix.alphalibary.annotations.Accessor;
import de.alphahelix.alphalibary.core.utilites.entity.EntityAge;
import de.alphahelix.alphalibary.core.utilites.entity.EntityBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

/**
 * Used to transfer the {@link de.alphahelix.alphalibary.annotations.entity.Entity} annotation into a actual {@link Entity}
 *
 * @author AlphaHelix
 * @version 1.0
 * @see de.alphahelix.alphalibary.annotations.entity.Entity
 * @see Entity
 * @since 1.9.2.1
 */
public class AnnotatedEntity {
	
	private final Object entityClazz;
	private final Field entityField;
	
	private final Class<? extends Entity> typeClazz;
	private final String name;
	private final double health;
	private final boolean moveable, pickupItem, glowing, gravity, invincible, ageLock;
	private final EntityAge age;
	
	private final Location location;
	
	public AnnotatedEntity(Object entityClazz, Field entityField, de.alphahelix.alphalibary.annotations.entity.Entity entity) {
		this.entityClazz = entityClazz;
		this.entityField = entityField;
		
		this.typeClazz = entity.typeClazz();
		this.name = entity.name();
		this.health = entity.health();
		this.moveable = entity.moveable();
		this.pickupItem = entity.pickUpItem();
		this.glowing = entity.glowing();
		this.gravity = entity.gravity();
		this.invincible = entity.invincible();
		this.ageLock = entity.ageLock();
		this.age = entity.age();
		
		this.location = new Location(Bukkit.getWorld(entity.world()), entity.x(), entity.y(), entity.z());
	}
	
	final AnnotatedEntity apply() {
		EntityBuilder builder = new EntityBuilder(typeClazz);
		
		if(!name.isEmpty())
			builder.setName(name);
		builder.setHealth(health).setMove(moveable).setItemPickup(pickupItem).setGlowing(glowing).setGravity(gravity).setInvincible(invincible).setAgeLock(ageLock).setAge(age);
		
		try {
			Accessor.access(entityField).set(entityClazz, builder.spawn(location));
		} catch(ReflectiveOperationException ignored) {
		}
		
		return this;
	}
}
