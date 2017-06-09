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
import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi.FakeMobType;
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;
import de.alphahelix.alphalibary.fakeapi.instances.FakeMob;
import de.alphahelix.alphalibary.fakeapi.utils.intern.FakeUtilBase;
import de.alphahelix.alphalibary.item.SkullItemBuilder;
import de.alphahelix.alphalibary.nms.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;

public class MobFakeUtil extends FakeUtilBase {

    private static HashMap<String, BukkitTask> followMap = new HashMap<>();
    private static HashMap<String, BukkitTask> stareMap = new HashMap<>();
    private static HashMap<String, BukkitTask> splitMap = new HashMap<>();

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

        if (fM == null) return null;

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
        try {
            Object mob = ReflectionUtil.getNmsClass(type.getNmsClass()).getConstructor(ReflectionUtil.getNmsClass("World")).newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(mob, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            if (baby) {
                setBaby().invoke(mob, -1);

                Object dw = getDataWatcher().invoke(mob);

                ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(mob), dw, true));
            }

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntityLiving().newInstance(mob));
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityHeadRotation().newInstance(mob, FakeAPI.toAngle(loc.getYaw())));
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityLook().newInstance(ReflectionUtil.getEntityID(mob), FakeAPI.toAngle(loc.getYaw()), FakeAPI.toAngle(loc.getPitch()), true));

            FakeMob fM = new FakeMob(loc, name, mob, type, baby);

            FakeAPI.addFakeMob(p, fM);
            return fM;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes a {@link FakeMob} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p   the {@link Player} to destroy the {@link FakeMob} for
     * @param mob the {@link FakeMob} to remove
     */
    public static void removeMob(Player p, FakeMob mob) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(mob.getNmsEntity())}));
            FakeAPI.removeFakeMob(p, mob);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutRelEntityMove().newInstance(
                    ReflectionUtil.getEntityID(mob.getNmsEntity()),
                    FakeAPI.toDelta((long) x),
                    FakeAPI.toDelta((long) y),
                    FakeAPI.toDelta((long) z),
                    false));

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityHeadRotation().newInstance(mob, FakeAPI.toAngle(yaw)));

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityLook().newInstance(ReflectionUtil.getEntityID(mob), FakeAPI.toAngle(yaw), FakeAPI.toAngle(pitch), true));

            mob.setCurrentlocation(mob.getCurrentlocation().add(x, y, z));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityTeleport().newInstance(a));

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityHeadRotation().newInstance(mob, FakeAPI.toAngle(loc.getYaw())));

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityLook().newInstance(ReflectionUtil.getEntityID(mob.getNmsEntity()), FakeAPI.toAngle(loc.getYaw()), FakeAPI.toAngle(loc.getPitch()), true));
            FakeAPI.getFakeMobByObject(p, mob).setCurrentlocation(loc);
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
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityEquipment().newInstance(
                    ReflectionUtil.getEntityID(mob.getNmsEntity()),
                    slot.getNmsSlot(),
                    ReflectionUtil.getObjectNMSItemStack(item)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a {@link FakeMob} follows a {@link Player}
     *
     * @param toCheck the {@link Player} to check if he has a {@link FakeMob} which follows him
     * @return if the {@link Player} has a {@link FakeMob} which followes him
     */
    public static boolean hasFollower(Player toCheck) {
        return followMap.containsKey(toCheck.getName());
    }

    /**
     * Make a {@link FakeMob} follow a specific {@link Player}, which only the {@link Player} can see
     *
     * @param p        the {@link Player} to see the following {@link FakeMob}
     * @param toFollow the {@link Player} which the {@link FakeMob} should follow
     * @param mob      the {@link FakeMob} which should follow the {@link Player}
     */
    public static void followPlayer(final Player p, final Player toFollow, final FakeMob mob) {
        followMap.put(p.getName(), new BukkitRunnable() {
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
        if (followMap.containsKey(p.getName())) {
            followMap.get(p.getName()).cancel();
            followMap.remove(p.getName());
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
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityHeadRotation().newInstance(mob.getNmsEntity(), FakeAPI.toAngle(LocationUtil.lookAt(mob.getCurrentlocation(), toLookAt.getLocation()).getYaw())));

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityLook().newInstance(
                    ReflectionUtil.getEntityID(mob.getNmsEntity()),
                    FakeAPI.toAngle(LocationUtil.lookAt(mob.getCurrentlocation(), toLookAt.getLocation()).getYaw()),
                    FakeAPI.toAngle(LocationUtil.lookAt(mob.getCurrentlocation(), toLookAt.getLocation()).getPitch()),
                    true));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a {@link FakeMob} stare at a specific Player, which another specific {@link Player} can see
     *
     * @param p         the {@link Player} to see the following watch
     * @param toStareAt the {@link Player} to stare at
     * @param mob       the {@link FakeMob} who should stare at the {@link Player}
     */
    public static void stareAtPlayer(final Player p, final Player toStareAt, final FakeMob mob) {
        try {
            stareMap.put(p.getName(), new BukkitRunnable() {
                @Override
                public void run() {
                    lookAtPlayer(p, toStareAt, mob);
                }
            }.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset the look for all {@link FakeMob}s which a specific {@link Player} can see
     *
     * @param p the {@link Player}
     */
    public static void normalizeLook(Player p) {
        if (stareMap.containsKey(p.getName())) {
            stareMap.get(p.getName()).cancel();
            stareMap.remove(p.getName());
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
        try {
            if (!FakeAPI.getFakeMobsInRadius(toAttack, 4).contains(mob)) return;

            lookAtPlayer(p, toAttack, mob);

            ReflectionUtil.sendPacket(p, getPacketPlayOutAnimation().newInstance(mob.getNmsEntity(), 0));

            toAttack.damage(damage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakeMob} for the {@link Player}
     *
     * @param p    the {@link Player} to see the new name of the {@link FakeMob}
     * @param name the actual new name of the {@link FakeMob}
     * @param mob  the {@link FakeMob} to change the name for
     */
    public static void setMobname(Player p, String name, FakeMob mob) {
        try {
            setCustomName().invoke(mob.getNmsEntity(), name.replace("&", "§").replace("_", " "));
            setCustomNameVisible().invoke(mob.getNmsEntity(), true);

            Object dw = getDataWatcher().invoke(mob.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(mob.getNmsEntity()), dw, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            final Location currentLocation = mob.getCurrentlocation();
            Vector between = to.toVector().subtract(currentLocation.toVector());

            final double toMoveInX = between.getX() / teleportCount;
            final double toMoveInY = between.getY() / teleportCount;
            final double toMoveInZ = between.getZ() / teleportCount;

            splitMap.put(p.getName(), new BukkitRunnable() {
                public void run() {
                    if (!LocationUtil.isSameLocation(currentLocation, to)) {
                        teleportMob(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), mob);
                    } else
                        this.cancel();
                }
            }.runTaskTimer(AlphaLibary.getInstance(), 0, wait));
        } catch (NullPointerException | IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.SEVERE, "[FakeAPI] Use {FakeEntity}.getNmsEntity() for the Object parameter!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels all teleport tasks for the {@link Player}
     *
     * @param p the {@link Player} to cancel all teleport tasks
     */
    public static void cancelAllSplittedTasks(Player p) {
        if (splitMap.containsKey(p.getName())) {
            splitMap.get(p.getName()).cancel();
            splitMap.remove(p.getName());
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
        try {
            setPassenger().invoke(mob.getNmsEntity(), entity.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutMount().newInstance(entity.getNmsEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
