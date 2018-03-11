package de.alphahelix.alphalibary.core.utilites.entity;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.Serializable;


public class EntityBuilder implements Serializable {

    private EntityType type;
    private Class<? extends Entity> entityClazz;
    private String name;
    private double health;
    private boolean move = true, itemPickup = false, glowing = false, gravity = true, invincible = false, ageLock = false;
    private EntityAge age;

    public EntityBuilder(EntityType type) {
        this.type = type;
    }

    public EntityBuilder(Class<? extends Entity> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public EntityBuilder setType(EntityType type) {
        this.type = type;
        return this;
    }

    public EntityBuilder setEntityClazz(Class<? extends Entity> entityClazz) {
        this.entityClazz = entityClazz;
        return this;
    }

    public EntityBuilder setName(String name) {
        this.name = name;
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

    public EntityBuilder setAgeLock(boolean ageLock) {
        this.ageLock = ageLock;
        return this;
    }

    public EntityBuilder setAge(EntityAge age) {
        this.age = age;
        return this;
    }

    public Entity spawn(Location where) {
        Entity e = null;
        if (type == null) {
            if (entityClazz != null) {
                e = where.getWorld().spawn(where, entityClazz);

                e.setCustomNameVisible(name == null);
                e.setCustomName(name == null ? "" : name);

                if (e instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) e;

                    le.setHealth(health == 0 ? le.getHealth() : health);

                    le.setAI(move);
                    le.setCanPickupItems(itemPickup);
                }

                e.setGlowing(glowing);
                e.setGravity(gravity);
                e.setInvulnerable(invincible);

                if (e instanceof Ageable) {
                    ((Ageable) e).setAge(age == null ? 1 : age.getBukkitAge());
                    ((Ageable) e).setAgeLock(ageLock);
                }
            }
        } else {
            e = where.getWorld().spawnEntity(where, type);

            e.setCustomNameVisible(name == null);
            e.setCustomName(name == null ? "" : name);

            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) e;

                le.setHealth(health == 0 ? le.getHealth() : health);

                le.setAI(move);
                le.setCanPickupItems(itemPickup);
            }

            e.setGlowing(glowing);
            e.setGravity(gravity);
            e.setInvulnerable(invincible);

            if (e instanceof Ageable) {
                ((Ageable) e).setAge(age == null ? 1 : age.getBukkitAge());
                ((Ageable) e).setAgeLock(ageLock);
            }
        }
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityBuilder that = (EntityBuilder) o;
        return Double.compare(that.health, health) == 0 &&
                move == that.move &&
                itemPickup == that.itemPickup &&
                glowing == that.glowing &&
                gravity == that.gravity &&
                invincible == that.invincible &&
                ageLock == that.ageLock &&
                type == that.type &&
                Objects.equal(entityClazz, that.entityClazz) &&
                Objects.equal(name, that.name) &&
                age == that.age;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, entityClazz, name, health, move, itemPickup, glowing, gravity, invincible, ageLock, age);
    }

    @Override
    public String toString() {
        return "EntityBuilder{" +
                "type=" + type +
                ", entityClazz=" + entityClazz +
                ", name='" + name + '\'' +
                ", health=" + health +
                ", move=" + move +
                ", itemPickup=" + itemPickup +
                ", glowing=" + glowing +
                ", gravity=" + gravity +
                ", invincible=" + invincible +
                ", ageLock=" + ageLock +
                ", age=" + age +
                '}';
    }
}
