package io.github.alphahelixdev.alpary.utilities.entity;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.Serializable;
import java.util.Objects;


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
	
	public EntityType getType() {
		return this.type;
	}
	
	public EntityBuilder setType(EntityType type) {
		this.type = type;
		return this;
	}
	
	public Class<? extends Entity> getEntityClazz() {
		return this.entityClazz;
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
	
	public String getName() {
		return this.name;
	}
	
	private void handleLivingEntity(Entity e) {
		LivingEntity le = (LivingEntity) e;
		
		le.setHealth(this.getHealth() == 0 ? le.getHealth() : this.getHealth());
		le.setAI(this.isMove());
		le.setCanPickupItems(this.isItemPickup());
	}
	
	public boolean isGlowing() {
		return this.glowing;
	}
	
	public EntityBuilder setGlowing(boolean glowing) {
		this.glowing = glowing;
		return this;
	}
	
	public boolean isGravity() {
		return this.gravity;
	}
	
	public EntityBuilder setGravity(boolean gravity) {
		this.gravity = gravity;
		return this;
	}
	
	public boolean isInvincible() {
		return this.invincible;
	}
	
	public EntityBuilder setInvincible(boolean invincible) {
		this.invincible = invincible;
		return this;
	}
	
	public EntityAge getAge() {
		return this.age;
	}
	
	public EntityBuilder setAge(EntityAge age) {
		this.age = age;
		return this;
	}
	
	public boolean isAgeLock() {
		return this.ageLock;
	}
	
	public EntityBuilder setAgeLock(boolean ageLock) {
		this.ageLock = ageLock;
		return this;
	}
	
	public double getHealth() {
		return this.health;
	}
	
	public EntityBuilder setHealth(double health) {
		this.health = health;
		return this;
	}
	
	public boolean isMove() {
		return this.move;
	}
	
	public EntityBuilder setMove(boolean move) {
		this.move = move;
		return this;
	}
	
	public boolean isItemPickup() {
		return this.itemPickup;
	}
	
	public EntityBuilder setItemPickup(boolean itemPickup) {
		this.itemPickup = itemPickup;
		return this;
	}
	
	public EntityBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getHealth(), this.isMove(), this.isItemPickup(), this.isGlowing(), this.isGravity(), this.isInvincible(), this.isAgeLock(), this.getName(), this.getType(), this.getAge(), this.getEntityClazz());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EntityBuilder that = (EntityBuilder) o;
		return Double.compare(that.getHealth(), getHealth()) == 0 &&
				this.isMove() == that.isMove() &&
				this.isItemPickup() == that.isItemPickup() &&
				this.isGlowing() == that.isGlowing() &&
				this.isGravity() == that.isGravity() &&
				this.isInvincible() == that.isInvincible() &&
				this.isAgeLock() == that.isAgeLock() &&
				Objects.equals(this.getName(), that.getName()) &&
				this.getType() == that.getType() &&
				this.getAge() == that.getAge() &&
				Objects.equals(this.getEntityClazz(), that.getEntityClazz());
	}
	
	@Override
	public String toString() {
		return "EntityBuilder{" +
				"type=" + this.type +
				", entityClazz=" + this.entityClazz +
				", name='" + this.name + '\'' +
				", health=" + this.health +
				", move=" + this.move +
				", itemPickup=" + this.itemPickup +
				", glowing=" + this.glowing +
				", gravity=" + this.gravity +
				", invincible=" + this.invincible +
				", ageLock=" + this.ageLock +
				", age=" + this.age +
				'}';
	}
}
