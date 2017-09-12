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
import de.alphahelix.alphalibary.item.SkullItemBuilder;
import de.alphahelix.alphalibary.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.nms.packets.*;
import de.alphahelix.alphalibary.nms.wrappers.EntityWrapper;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ArmorstandFakeUtil {

    private static final HashMap<String, BukkitTask> FOLLOW_MAP = new HashMap<>();
    private static final HashMap<String, BukkitTask> SPLIT_MAP = new HashMap<>();

    private static final ReflectionUtil.SaveConstructor ENTITY_ARMORSTAND =
            ReflectionUtil.getDeclaredConstructor("EntityArmorStand", ReflectionUtil.getNmsClass("World"));

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
        Object armorstand = ENTITY_ARMORSTAND.newInstance(false, ReflectionUtil.getWorldServer(loc.getWorld()));
        EntityWrapper aW = new EntityWrapper(armorstand);

        aW.setLocation(loc);
        aW.setInvisible(true);
        aW.setCustomName(name);
        aW.setCustomNameVisible(true);

        ReflectionUtil.sendPacket(p, new PPOSpawnEntityLiving(armorstand).getPacket(false));

        FakeArmorstand fA = new FakeArmorstand(loc, name, armorstand);

        FakeAPI.addFakeArmorstand(p, fA);
        return fA;
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
        Location old = armorstand.getCurrentlocation();
        Location ne = old.clone().add(x, y, z);

        ReflectionUtil.sendPacket(p, new PPORelEntityMove(
                ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                old.getX() - ne.getX(), old.getY() - ne.getY(), old.getZ() - ne.getZ(), false
        ).getPacket(false));

        armorstand.setCurrentlocation(armorstand.getCurrentlocation().add(x, y, z));
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

        SPLIT_MAP.put(p.getName(), new BukkitRunnable() {
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
        if (SPLIT_MAP.containsKey(p.getName())) {
            SPLIT_MAP.get(p.getName()).cancel();
            SPLIT_MAP.remove(p.getName());
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

            ReflectionUtil.sendPacket(p, new PPOEntityTeleport(a).getPacket(false));

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
        ReflectionUtil.sendPacket(p, new PPOEntityEquipment(
                ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                item, slot
        ).getPacket(false));
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
        equipArmorstand(p, armorstand, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
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
        equipArmorstand(p, armorstand, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
    }

    /**
     * Make a {@link FakeArmorstand} follow a specific {@link Player}, which only the {@link Player} can see
     *
     * @param p          the {@link Player} to see the following {@link FakeArmorstand}
     * @param toFollow   the {@link Player} which the {@link FakeArmorstand} should follow
     * @param armorstand the {@link FakeArmorstand} which should follow the {@link Player}
     */
    public static void followArmorstand(final Player p, final Player toFollow, final FakeArmorstand armorstand) {
        FOLLOW_MAP.put(p.getName(), new BukkitRunnable() {
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
        if (FOLLOW_MAP.containsKey(p.getName())) {
            FOLLOW_MAP.get(p.getName()).cancel();
            FOLLOW_MAP.remove(p.getName());
        }
    }

    /**
     * Removes a {@link FakeArmorstand} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p          the {@link Player} to destroy the {@link FakeArmorstand} for
     * @param armorstand the {@link FakeArmorstand} to remove
     */
    public static void destroyArmorstand(Player p, FakeArmorstand armorstand) {
        ReflectionUtil.sendPacket(p, new PPOEntityDestroy(ReflectionUtil.getEntityID(armorstand.getNmsEntity())).getPacket(false));
        FakeAPI.removeFakeArmorstand(p, armorstand);
    }

    /**
     * Sets a new name for the {@link FakeArmorstand} for the {@link Player}
     *
     * @param p          the {@link Player} to see the new name of the {@link FakeArmorstand}
     * @param name       the actual new name of the {@link FakeArmorstand}
     * @param armorstand the {@link FakeArmorstand} to change the name for
     */
    public static void setArmorstandname(Player p, String name, FakeArmorstand armorstand) {
        EntityWrapper a = new EntityWrapper(armorstand.getNmsEntity());

        a.setCustomName(name.replace("&", "§").replace("_", " "));

        Object dw = a.getDataWatcher();

        ReflectionUtil.sendPacket(p, new PPOEntityMetadata(
                ReflectionUtil.getEntityID(armorstand.getNmsEntity()),
                dw
        ).getPacket(false));
    }
}
