package io.github.alphahelixdev.alpary.utilities.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
public class EntityBuilder implements Serializable {
	
	private double health;
	private boolean move = true, itemPickup = false, glowing = false, gravity = true, invincible = false, ageLock = false;
	private String name;
	
	private EntityType type;
	private EntityAge age;
	private Class<? extends Entity> entityClazz;
	
	public EntityBuilder(EntityType type) {
		this.setType(type);
	}
	
	public EntityBuilder(Class<? extends Entity> entityClazz) {
		this.setEntityClazz(entityClazz);
	}
	
	public Entity spawn(Location where) {
		Entity e;
		if(this.getType() == null)
			if(this.getEntityClazz() != null)
				e = where.getWorld().spawn(where, this.getEntityClazz());
			else
				throw new IllegalArgumentException("Need to specify either a type or a entity class");
		else
			e = where.getWorld().spawnEntity(where, this.getType());
		
		return e;
	}
	
	public EntityBuilder setType(EntityType type) {
		this.type = type;
		return this;
	}
	
	public EntityBuilder setEntityClazz(Class<? extends Entity> entityClazz) {
		this.entityClazz = entityClazz;
		return this;
	}
	
	private void handlePostSpawn(Entity e) {
		e.setCustomNameVisible(this.getName() == null);
		e.setCustomName(this.getName() == null ? "" : this.getName());
		
		if(e instanceof LivingEntity)
			handleLivingEntity(e);
		
		e.setGlowing(this.isGlowing());
		e.setGravity(this.isGravity());
		e.setInvulnerable(this.isInvincible());
		
		if(e instanceof Ageable) {
			((Ageable) e).setAge(this.getAge() == null ? 1 : this.getAge().getBukkitAge());
			((Ageable) e).setAgeLock(this.isAgeLock());
		}
	}
	
	private void handleLivingEntity(Entity e) {
		LivingEntity le = (LivingEntity) e;
		
		le.setHealth(this.getHealth() == 0 ? le.getHealth() : this.getHealth());
		le.setAI(this.isMove());
		le.setCanPickupItems(this.isItemPickup());
	}
	
	public EntityBuilder setGlowing(boolean glowing) {
		this.glowing = glowing;
		return this;
	}
	
	public EntityBuilder setGravity(boolean gravity) {
		this.gravity = gravity;
		return this;
	}
	
	public EntityBuilder setInvincible(boolean invincible) {
		this.invincible = invincible;
		return this;
	}
	
	public EntityBuilder setAge(EntityAge age) {
		this.age = age;
		return this;
	}
	
	public EntityBuilder setAgeLock(boolean ageLock) {
		this.ageLock = ageLock;
		return this;
	}
	
	public EntityBuilder setHealth(double health) {
		this.health = health;
		return this;
	}
	
	public EntityBuilder setMove(boolean move) {
		this.move = move;
		return this;
	}
	
	public EntityBuilder setItemPickup(boolean itemPickup) {
		this.itemPickup = itemPickup;
		return this;
	}
	
	public EntityBuilder setName(String name) {
		this.name = name;
		return this;
	}
}
