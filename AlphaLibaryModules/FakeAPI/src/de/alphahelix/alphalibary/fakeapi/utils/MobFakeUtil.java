/*
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.alphahelix.alphalibary.fakeapi.utils;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.LocationUtil;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi.FakeMobType;
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;
import de.alphahelix.alphalibary.fakeapi.instances.FakeMob;
import de.alphahelix.alphalibary.inventories.item.SkullItemBuilder;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.nms.packets.*;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityAgeableWrapper;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class MobFakeUtil {

    private static final HashMap<String, BukkitTask> FOLLOW_MAP = new HashMap<>();
    private static final HashMap<String, BukkitTask> STARE_MAP = new HashMap<>();
    private static final HashMap<String, BukkitTask> SPLIT_MAP = new HashMap<>();

    /**
     * Spawns in a {@link FakeMob} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeMob} for
     * @param loc  {@link Location} where the {@link FakeMob} should be spawned at
     * @param name of the {@link FakeMob} inside the file
     * @param type the {@link FakeMobType} of the {@link FakeMob} to spawn
     * @param baby if the mob should be tiny
     * @return the new spawned {@link FakeMob}
     */
    public static FakeMob spawnMob(Player p, Location loc, String name, FakeMobType type, boolean baby) {
        FakeMob fM = spawnTemporaryMob(p, loc, name, type, baby);

        FakeRegister.getMobLocationsFile().addMobToFile(fM);

        return fM;
    }

    /**
     * Spawns in a temporary {@link FakeMob} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeMob} for
     * @param loc  {@link Location} where the {@link FakeMob} should be spawned at
     * @param name of the {@link FakeMob} inside the file
     * @param type the {@link FakeMobType} of the {@link FakeMob} to spawn
     * @param baby if the mob should be tiny
     * @return the new spawned {@link FakeMob}
     */
    public static FakeMob spawnTemporaryMob(Player p, Location loc, String name, FakeMobType type, boolean baby) {
        Object mob = ReflectionUtil.getDeclaredConstructor(type.getNmsClass(), ReflectionUtil.getNmsClass("World"))
                .newInstance(false, ReflectionUtil.getWorldServer(loc.getWorld()));
        EntityWrapper m = new EntityWrapper(mob);

        m.setLocation(loc);

        if (baby) {
            EntityAgeableWrapper mA = new EntityAgeableWrapper(mob);

            mA.setAge(-1);

            ReflectionUtil.sendPacket(p, new PPOEntityMetadata(mA.getEntityID(), mA.getDataWatcher()));
        }

        ReflectionUtil.sendPacket(p, new PPOSpawnEntityLiving(mob));
        ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(mob, loc.getYaw()));
        ReflectionUtil.sendPacket(p, new PPOEntityLook(m.getEntityID(), loc.getYaw(), loc.getPitch(), true));

        FakeMob fM = new FakeMob(loc, name, mob, type, baby);

        FakeAPI.addFakeEntity(p, fM);
        return fM;
    }

    /**
     * Removes a {@link FakeMob} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p   the {@link Player} to destroy the {@link FakeMob} for
     * @param mob the {@link FakeMob} to remove
     */
    public static void removeMob(Player p, FakeMob mob) {
        ReflectionUtil.sendPacket(p, new PPOEntityDestroy(ReflectionUtil.getEntityID(mob.getNmsEntity())));
        FakeAPI.removeFakeEntity(p, mob);
    }

    /**
     * Moves the given {@link FakeMob}
     *
     * @param p     the {@link Player} to move the {@link FakeMob} for
     * @param x     blocks in x direction
     * @param y     blocks in y direction
     * @param z     blocks in z direction
     * @param yaw   new yaw
     * @param pitch new pitch
     * @param mob   the {@link FakeMob} which should be moved
     */
    public static void moveMob(Player p, double x, double y, double z, float yaw, float pitch, FakeMob mob) {
        Location old = mob.getCurrentlocation();
        Location ne = old.clone().add(x, y, z);

        ReflectionUtil.sendPacket(p, new PPORelEntityMove(
                ReflectionUtil.getEntityID(mob.getNmsEntity()),
                old.getX() - ne.getX(),
                old.getY() - ne.getY(),
                old.getY() - ne.getY(),
                false
        ));
        ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(mob.getNmsEntity(), yaw));
        ReflectionUtil.sendPacket(p, new PPOEntityLook(ReflectionUtil.getEntityID(mob.getNmsEntity()), yaw, pitch, false));

        mob.setCurrentlocation(mob.getCurrentlocation().add(x, y, z));
    }

    /**
     * Teleports a {@link FakeMob} to a specific {@link Location} for the given {@link Player}
     *
     * @param p   the {@link Player} to teleport the {@link FakeMob} for
     * @param loc the {@link Location} to teleport the {@link FakeMob} to
     * @param mob the {@link FakeMob} which should be teleported
     */
    public static void teleportMob(Player p, Location loc, FakeMob mob) {
        try {
            Field x = ReflectionUtil.getNmsClass("Entity").getField("locX"), y = ReflectionUtil.getNmsClass("Entity").getField("locY"), z = ReflectionUtil.getNmsClass("Entity").getField("locZ"), yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw"), pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
            Object a = mob.getNmsEntity();

            x.setAccessible(true);
            y.setAccessible(true);
            z.setAccessible(true);
            yaw.setAccessible(true);
            pitch.setAccessible(true);

            x.set(a, loc.getX());
            y.set(a, loc.getY());
            z.set(a, loc.getZ());
            yaw.set(a, loc.getYaw());
            pitch.set(a, loc.getPitch());

            ReflectionUtil.sendPacket(p, new PPOEntityTeleport(a));
            ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(a, loc.getYaw()));
            ReflectionUtil.sendPacket(p, new PPOEntityLook(ReflectionUtil.getEntityID(a), loc.getYaw(), loc.getPitch(), false));

            FakeAPI.getFakeEntityByObject(p, mob).setCurrentlocation(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Equip a {@link FakeMob} with a {@link ItemStack} for the {@link Player}
     *
     * @param p    the {@link Player} to equip the {@link FakeMob} for
     * @param mob  the {@link FakeMob} which should get equipped
     * @param item the {@link ItemStack} which the {@link FakeMob} should receive
     * @param slot the {@link REnumEquipSlot} where the {@link ItemStack} should be placed at
     */
    public static void equipMob(Player p, FakeMob mob, ItemStack item, REnumEquipSlot slot) {
        ReflectionUtil.sendPacket(p, new PPOEntityEquipment(
                ReflectionUtil.getEntityID(mob.getNmsEntity()),
                item,
                slot
        ));
    }

    /**
     * Check if a {@link FakeMob} follows a {@link Player}
     *
     * @param toCheck the {@link Player} to check if he has a {@link FakeMob} which follows him
     * @return if the {@link Player} has a {@link FakeMob} which followes him
     */
    public static boolean hasFollower(Player toCheck) {
        return FOLLOW_MAP.containsKey(toCheck.getName());
    }

    /**
     * Make a {@link FakeMob} follow a specific {@link Player}, which only the {@link Player} can see
     *
     * @param p        the {@link Player} to see the following {@link FakeMob}
     * @param toFollow the {@link Player} which the {@link FakeMob} should follow
     * @param mob      the {@link FakeMob} which should follow the {@link Player}
     */
    public static void followPlayer(final Player p, final Player toFollow, final FakeMob mob) {
        FOLLOW_MAP.put(p.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                teleportMob(p, LocationUtil.getLocationBehindPlayer(toFollow, 2), mob);
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
    }

    /**
     * Make a {@link FakeMob} unfollow his {@link Player}
     *
     * @param p the {@link Player} who shouldn't be followed any longer
     */
    public static void unFollowPlayer(Player p) {
        if (FOLLOW_MAP.containsKey(p.getName())) {
            FOLLOW_MAP.get(p.getName()).cancel();
            FOLLOW_MAP.remove(p.getName());
        }
    }

    /**
     * Make a {@link FakeMob} look at a specific Player, which another specific {@link Player} can see
     *
     * @param p        the {@link Player} to see the following watch
     * @param toLookAt the {@link Player} to look at
     * @param mob      the {@link FakeMob} who should watch the {@link Player}
     */
    public static void lookAtPlayer(Player p, Player toLookAt, FakeMob mob) {
        ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(mob.getNmsEntity(), LocationUtil.lookAt(mob.getCurrentlocation(), toLookAt.getLocation()).getYaw()));
        ReflectionUtil.sendPacket(p, new PPOEntityLook(ReflectionUtil.getEntityID(mob.getNmsEntity()),
                LocationUtil.lookAt(mob.getCurrentlocation(), toLookAt.getLocation()).getYaw(),
                LocationUtil.lookAt(mob.getCurrentlocation(), toLookAt.getLocation()).getPitch(), true));
    }

    /**
     * Make a {@link FakeMob} stare at a specific Player, which another specific {@link Player} can see
     *
     * @param p         the {@link Player} to see the following watch
     * @param toStareAt the {@link Player} to stare at
     * @param mob       the {@link FakeMob} who should stare at the {@link Player}
     */
    public static void stareAtPlayer(final Player p, final Player toStareAt, final FakeMob mob) {
        STARE_MAP.put(p.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                lookAtPlayer(p, toStareAt, mob);
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
    }

    /**
     * Reset the look for all {@link FakeMob}s which a specific {@link Player} can see
     *
     * @param p the {@link Player}
     */
    public static void normalizeLook(Player p) {
        if (STARE_MAP.containsKey(p.getName())) {
            STARE_MAP.get(p.getName()).cancel();
            STARE_MAP.remove(p.getName());
        }
    }

    /**
     * Make a {@link FakeMob} attack a {@link Player}
     *
     * @param p        the {@link Player} whp can see the attack
     * @param toAttack the {@link Player} who should be attacked
     * @param mob      the {@link FakeMob} who should attack
     * @param damage   the damage which should be done by the {@link FakeMob}
     */
    public static void attackPlayer(Player p, Player toAttack, FakeMob mob, double damage) {
        if (!FakeAPI.getFakeEntitysInRadius(toAttack, 4).contains(mob)) return;

        lookAtPlayer(p, toAttack, mob);

        ReflectionUtil.sendPacket(p, new PPOAnimation(mob.getNmsEntity(), 0));

        toAttack.damage(damage);
    }

    /**
     * Sets a new name for the {@link FakeMob} for the {@link Player}
     *
     * @param p    the {@link Player} to see the new name of the {@link FakeMob}
     * @param name the actual new name of the {@link FakeMob}
     * @param mob  the {@link FakeMob} to change the name for
     */
    public static void setMobname(Player p, String name, FakeMob mob) {
        EntityWrapper e = new EntityWrapper(mob.getNmsEntity());

        e.setCustomName(name.replace("&", "§").replace("_", " "));
        e.setCustomNameVisible(true);

        ReflectionUtil.sendPacket(p, new PPOEntityMetadata(e.getEntityID(), e.getDataWatcher()));
    }

    /**
     * Teleport a {@link FakeMob} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakeMob} for
     * @param to            the {@link Location} where the {@link FakeMob} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param mob           the {@link FakeMob} which should be teleported
     */
    public static void splitTeleportMob(final Player p, final Location to, final int teleportCount, final long wait, final FakeMob mob) {
        final Location currentLocation = mob.getCurrentlocation();
        Vector between = to.toVector().subtract(currentLocation.toVector());

        final double toMoveInX = between.getX() / teleportCount;
        final double toMoveInY = between.getY() / teleportCount;
        final double toMoveInZ = between.getZ() / teleportCount;

        SPLIT_MAP.put(p.getName(), new BukkitRunnable() {
            public void run() {
                if (!LocationUtil.isSameLocation(currentLocation, to)) {
                    teleportMob(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), mob);
                } else
                    this.cancel();
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0, wait));
    }

    /**
     * Cancels all teleport tasks for the {@link Player}
     *
     * @param p the {@link Player} to cancel all teleport tasks
     */
    public static void cancelAllSplittedTasks(Player p) {
        if (SPLIT_MAP.containsKey(p.getName())) {
            SPLIT_MAP.get(p.getName()).cancel();
            SPLIT_MAP.remove(p.getName());
        }
    }

    /**
     * Sets the head of the {@link FakeMob} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p          the {@link Player} to show the custom Skull
     * @param mob        the {@link FakeMob} which should get equipped
     * @param textureURL the URL where to find the plain 1.7 skin
     */
    public static void equipMobSkull(Player p, FakeMob mob, String textureURL) {
        equipMob(p, mob, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
    }

    /**
     * Sets the head of the {@link FakeMob} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p       the {@link Player} to show the custom Skull
     * @param mob     the {@link FakeMob} which should get equipped
     * @param profile the {@link GameProfile} of the owner of the skull
     */
    public static void equipMobSkull(Player p, FakeMob mob, GameProfile profile) {
        equipMob(p, mob, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
    }


    /**
     * Set the {@link FakeEntity} as a passenger of the {@link FakeMob}
     *
     * @param p      the {@link Player} to show it
     * @param mob    the {@link FakeMob} which should carry the {@link FakeEntity}
     * @param entity the {@link FakeEntity} which should ride the {@link FakeMob}
     */
    public static void ride(Player p, FakeMob mob, FakeEntity entity) {
        EntityWrapper e = new EntityWrapper(mob.getNmsEntity());

        e.startRiding(entity.getNmsEntity());

        ReflectionUtil.sendPacket(p, new PPOMount(entity.getNmsEntity()));
    }
}
