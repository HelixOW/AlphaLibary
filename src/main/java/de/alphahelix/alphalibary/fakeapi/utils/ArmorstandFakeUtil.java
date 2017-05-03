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
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakeArmorstand;
import de.alphahelix.alphalibary.fakeapi.utils.intern.FakeUtilBase;
import de.alphahelix.alphalibary.item.SkullItemBuilder;
import de.alphahelix.alphalibary.nms.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.LocationUtil;
import de.alphahelix.alphalibary.utils.MinecraftVersion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ArmorstandFakeUtil extends FakeUtilBase {

    private static HashMap<String, BukkitTask> followMap = new HashMap<>();
    private static HashMap<String, BukkitTask> splitMap = new HashMap<>();

    private static Constructor<?> entityArmorstand;

    static {
        try {
            entityArmorstand = ReflectionUtil.getNmsClass("EntityArmorStand").getConstructor(ReflectionUtil.getNmsClass("World"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns in a {@link FakeArmorstand} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeArmorstand} for
     * @param loc  {@link Location} where the {@link FakeArmorstand} should be spawned at
     * @param name of the {@link FakeArmorstand} inside the file and above his head
     * @return the new spawned {@link FakeArmorstand}
     */
    public static FakeArmorstand spawnArmorstand(Player p, Location loc, String name) {
        FakeArmorstand fakeA = spawnTemporaryArmorstand(p, loc, name);

        if (fakeA == null)
            return null;

        FakeRegister.getArmorstandLocationsFile().addArmorstandToFile(fakeA);
        return fakeA;
    }

    /**
     * Spawns in a temporary {@link FakeArmorstand} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeArmorstand} for
     * @param loc  {@link Location} where the {@link FakeArmorstand} should be spawned at
     * @param name of the {@link FakeArmorstand} inside the file and above his head
     * @return the new spawned {@link FakeArmorstand}
     */
    public static FakeArmorstand spawnTemporaryArmorstand(Player p, Location loc, String name) {
        try {
            Object armorstand = entityArmorstand.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(armorstand, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            setInvisible().invoke(armorstand, true);
            setCustomName().invoke(armorstand, name);
            setCustomNameVisible().invoke(armorstand, true);

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntityLiving().newInstance(armorstand));

            FakeArmorstand fA = new FakeArmorstand(loc, name, armorstand);

            FakeAPI.addFakeArmorstand(p, fA);
            return fA;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Moves the given {@link FakeArmorstand}
     *
     * @param p          the {@link Player} to move the {@link FakeArmorstand} for
     * @param x          blocks in x direction
     * @param y          blocks in y direction
     * @param z          blocks in z direction
     * @param armorstand the {@link FakeArmorstand} which should be moved
     */
    public static void moveArmorstand(Player p, double x, double y, double z, FakeArmorstand armorstand) {
        try {
            if (VERSION != MinecraftVersion.EIGHT) {
                ReflectionUtil.sendPacket(p, getPacketPlayOutRelEntityMove().newInstance(
                        ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                        (long) FakeAPI.toDelta(x),
                        (long) FakeAPI.toDelta(y),
                        (long) FakeAPI.toDelta(z),
                        true));
            } else {
                ReflectionUtil.sendPacket(p, getPacketPlayOutRelEntityMove().newInstance(
                        ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                        ((byte) (x * 32)),
                        ((byte) (y * 32)),
                        ((byte) (z * 32)),
                        true));
            }
            armorstand.setCurrentlocation(armorstand.getCurrentlocation().add(x, y, z));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teleport a {@link FakeArmorstand} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakeArmorstand} for
     * @param to            the {@link Location} where the {@link FakeArmorstand} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param armorstand    the {@link FakeArmorstand} which should be teleported
     */
    public static void splitTeleportArmorstand(final Player p, final Location to, final int teleportCount, final long wait, final FakeArmorstand armorstand) {
        final Location currentLocation = armorstand.getCurrentlocation();
        Vector between = to.toVector().subtract(currentLocation.toVector());

        final double toMoveInX = between.getX() / teleportCount;
        final double toMoveInY = between.getY() / teleportCount;
        final double toMoveInZ = between.getZ() / teleportCount;

        splitMap.put(p.getName(), new BukkitRunnable() {
            public void run() {
                if (!LocationUtil.isSameLocation(currentLocation, to)) {
                    teleportArmorstand(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), armorstand);
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
        if (splitMap.containsKey(p.getName())) {
            splitMap.get(p.getName()).cancel();
            splitMap.remove(p.getName());
        }
    }

    /**
     * Teleports a {@link FakeArmorstand} to a specific {@link Location} for the given {@link Player}
     *
     * @param p          the {@link Player} to teleport the {@link FakeArmorstand} for
     * @param loc        the {@link Location} to teleport the {@link FakeArmorstand} to
     * @param armorstand the {@link FakeArmorstand} which should be teleported
     */
    public static void teleportArmorstand(Player p, Location loc, FakeArmorstand armorstand) {
        try {
            if (VERSION != MinecraftVersion.EIGHT) {

                Field x = ReflectionUtil.getNmsClass("Entity").getField("locX"), y = ReflectionUtil.getNmsClass("Entity").getField("locY"), z = ReflectionUtil.getNmsClass("Entity").getField("locZ"), yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw"), pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
                Object a = armorstand.getNmsEntity();

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
            } else {
                ReflectionUtil.sendPacket(p, getPacketPlayOutEntityTeleport().newInstance(
                        ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                        FakeAPI.floor(loc.getBlockX() * 32.0D),
                        FakeAPI.floor(loc.getBlockY() * 32.0D),
                        FakeAPI.floor(loc.getBlockZ() * 32.0D),
                        (byte) ((int) (loc.getYaw() * 256.0F / 360.0F)),
                        (byte) ((int) (loc.getPitch() * 256.0F / 360.0F)),
                        true));
            }

            armorstand.setCurrentlocation(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Equip a {@link FakeArmorstand} with a {@link ItemStack} for the {@link Player}
     *
     * @param p          the {@link Player} to equip the {@link FakeArmorstand} for
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param item       the {@link ItemStack} which the {@link FakeArmorstand} should receive
     * @param slot       the {@link REnumEquipSlot} where the {@link ItemStack} should be placed at
     */
    public static void equipArmorstand(Player p, FakeArmorstand armorstand, ItemStack item, REnumEquipSlot slot) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityEquipment().newInstance(
                    ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                    slot.getNmsSlot(),
                    ReflectionUtil.getObjectNMSItemStack(item)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the head of the {@link FakeArmorstand} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p          the {@link Player} to show the custom Skull
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param textureURL the URL where to find the plain 1.7 skin
     */
    public static void equipArmorstandSkull(Player p, FakeArmorstand armorstand, String textureURL) {
        equipArmorstand(p, armorstand, SkullItemBuilder.getSkull(textureURL), REnumEquipSlot.HELMET);
    }

    /**
     * Sets the head of the {@link FakeArmorstand} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p          the {@link Player} to show the custom Skull
     * @param armorstand the {@link FakeArmorstand} which should get equipped
     * @param profile    the {@link GameProfile} of the owner of the skull
     */
    public static void equipArmorstandSkull(Player p, FakeArmorstand armorstand, GameProfile profile) {
        try {
            equipArmorstand(p, armorstand, SkullItemBuilder.getSkull(profile), REnumEquipSlot.HELMET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a {@link FakeArmorstand} follow a specific {@link Player}, which only the {@link Player} can see
     *
     * @param p          the {@link Player} to see the following {@link FakeArmorstand}
     * @param toFollow   the {@link Player} which the {@link FakeArmorstand} should follow
     * @param armorstand the {@link FakeArmorstand} which should follow the {@link Player}
     */
    public static void followArmorstand(final Player p, final Player toFollow, final FakeArmorstand armorstand) {
        followMap.put(p.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                teleportArmorstand(p, toFollow.getLocation(), armorstand);
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
    }

    /**
     * Make a {@link FakeArmorstand} unfollow his {@link Player}
     *
     * @param p the {@link Player} who shouldn't be followed anylonger
     */
    public static void unFollowArmorstand(Player p) {
        if (followMap.containsKey(p.getName())) {
            followMap.get(p.getName()).cancel();
            followMap.remove(p.getName());
        }
    }

    /**
     * Removes a {@link FakeArmorstand} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p          the {@link Player} to destroy the {@link FakeArmorstand} for
     * @param armorstand the {@link FakeArmorstand} to remove
     */
    public static void destroyArmorstand(Player p, FakeArmorstand armorstand) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(armorstand.getNmsEntity())}));
            FakeAPI.removeFakeArmorstand(p, armorstand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakeArmorstand} for the {@link Player}
     *
     * @param p          the {@link Player} to see the new name of the {@link FakeArmorstand}
     * @param name       the actual new name of the {@link FakeArmorstand}
     * @param armorstand the {@link FakeArmorstand} to change the name for
     */
    public static void setArmorstandname(Player p, String name, FakeArmorstand armorstand) {
        try {
            setCustomName().invoke(armorstand.getNmsEntity(), name.replace("&", "§").replace("_", " "));

            Object dw = getDataWatcher().invoke(armorstand.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(armorstand.getNmsEntity()), dw, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
