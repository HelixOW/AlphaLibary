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

import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi.FakeMobType;
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakeBigItem;
import de.alphahelix.alphalibary.fakeapi.instances.FakeMob;
import de.alphahelix.alphalibary.fakeapi.utils.intern.FakeUtilBase;
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

public class BigItemFakeUtil extends FakeUtilBase {

    private static HashMap<String, BukkitTask> splitMap = new HashMap<>();

    /**
     * Spawns in a {@link FakeBigItem} for the {@link Player}
     *
     * @param p         the {@link Player} to spawn the {@link FakeBigItem} for
     * @param loc       {@link Location} where the {@link FakeBigItem} should be spawned at
     * @param name      of the {@link FakeBigItem} inside the file and above his head
     * @param itemStack the {@link ItemStack} which should be shown
     * @return the new spawned {@link FakeBigItem}
     */
    public static FakeBigItem spawnBigItem(Player p, Location loc, String name, ItemStack itemStack) {
        FakeBigItem fBI = spawnTemporaryBigItem(p, loc, name, itemStack);

        if (fBI == null)
            return null;

        FakeRegister.getBigItemLocationsFile().addBigItemToFile(fBI);

        return fBI;
    }


    /**
     * Spawns in a temporary {@link FakeBigItem} (disappears after rejoin) for the {@link Player}
     *
     * @param p     the {@link Player} to spawn the {@link FakeBigItem} for
     * @param loc   {@link Location} where the {@link FakeBigItem} should be spawned at
     * @param name  of the {@link FakeBigItem} inside the file and above his head
     * @param stack the {@link ItemStack} which should be shown
     * @return the new spawned {@link FakeBigItem}
     */
    public static FakeBigItem spawnTemporaryBigItem(Player p, Location loc, String name, ItemStack stack) {
        try {
            FakeMob fakeGiant = MobFakeUtil.spawnTemporaryMob(p, loc, name, FakeMobType.GIANT, false);
            assert fakeGiant != null;
            Object giant = fakeGiant.getNmsEntity();
            Object dw = getDataWatcher().invoke(giant);

            setInvisible().invoke(giant, true);

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(giant), dw, true));

            MobFakeUtil.equipMob(p, fakeGiant, stack, REnumEquipSlot.HAND);

            FakeBigItem fBI = new FakeBigItem(loc, name, giant, stack);

            FakeAPI.addFakeBigItem(p, fBI);
            return fBI;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes a {@link FakeBigItem} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p    the {@link Player} to destroy the {@link FakeBigItem} for
     * @param item the {@link FakeBigItem} to remove
     */
    public static void destroyBigItem(Player p, FakeBigItem item) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(item.getNmsEntity())}));
            FakeAPI.removeFakeBigItem(p, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teleport a {@link FakeBigItem} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakeBigItem} for
     * @param to            the {@link Location} where the {@link FakeBigItem} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param item          the {@link FakeBigItem} which should be teleported
     */
    public static void splitTeleportBigItem(final Player p, final Location to, final int teleportCount, final long wait, final FakeBigItem item) {
        try {
            final Location currentLocation = item.getCurrentlocation();
            Vector between = to.toVector().subtract(currentLocation.toVector());

            final double toMoveInX = between.getX() / teleportCount;
            final double toMoveInY = between.getY() / teleportCount;
            final double toMoveInZ = between.getZ() / teleportCount;

            splitMap.put(p.getName(), new BukkitRunnable() {
                public void run() {
                    if (!LocationUtil.isSameLocation(currentLocation, to)) {
                        teleportBigItem(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), item);
                    } else
                        this.cancel();
                }
            }.runTaskTimer(AlphaLibary.getInstance(), 0, wait));
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
     * Teleports a {@link FakeBigItem} to a specific {@link Location} for the given {@link Player}
     *
     * @param p    the {@link Player} to teleport the {@link FakeBigItem} for
     * @param loc  the {@link Location} to teleport the {@link FakeBigItem} to
     * @param item the {@link FakeBigItem} which should be teleported
     */
    public static void teleportBigItem(Player p, Location loc, FakeBigItem item) {
        try {
            Field x = ReflectionUtil.getNmsClass("Entity").getField("locX"), y = ReflectionUtil.getNmsClass("Entity").getField("locY"), z = ReflectionUtil.getNmsClass("Entity").getField("locZ"), yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw"), pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
            Object a = item.getNmsEntity();

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

            item.setCurrentlocation(loc);
        } catch (NullPointerException | IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.SEVERE, "[FakeAPI] Use {FakeEntity}.getNmsEntity() for the Object parameter!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
