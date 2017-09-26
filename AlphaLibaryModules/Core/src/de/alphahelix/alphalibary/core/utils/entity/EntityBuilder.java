package de.alphahelix.alphalibary.core.utils.entity;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.Serializable;

@SuppressWarnings("ALL")
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

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setEntityClazz(Class<? extends Entity> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public void setItemPickup(boolean itemPickup) {
        this.itemPickup = itemPickup;
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public void setAge(EntityAge age) {
        this.age = age;
    }

    public void setAgeLock(boolean ageLock) {
        this.ageLock = ageLock;
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
