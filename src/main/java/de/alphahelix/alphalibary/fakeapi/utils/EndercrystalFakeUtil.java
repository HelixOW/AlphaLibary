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
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakeEndercrystal;
import de.alphahelix.alphalibary.fakeapi.utils.intern.FakeUtilBase;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.LocationUtil;
import de.alphahelix.alphalibary.utils.MinecraftVersion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

public class EndercrystalFakeUtil extends FakeUtilBase {

    private static HashMap<String, BukkitTask> splitMap = new HashMap<>();

    private static Constructor<?> entityEndercrystal;

    static {
        try {
            entityEndercrystal = ReflectionUtil.getNmsClass("EntityEnderCrystal").getConstructor(ReflectionUtil.getNmsClass("World"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns in a {@link FakeEndercrystal} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeEndercrystal} for
     * @param loc  {@link Location} where the {@link FakeEndercrystal} should be spawned at
     * @param name of the {@link FakeEndercrystal} inside the file and above his head
     * @return the new spawned {@link FakeEndercrystal}
     */
    public static FakeEndercrystal spawnEndercrystal(Player p, Location loc, String name) {
        FakeEndercrystal fE = spawnTemporaryEndercrystal(p, loc, name);

        if (fE == null)
            return null;

        FakeRegister.getEndercrystalLocationsFile().addEndercrystalToFile(fE);

        return fE;
    }

    /**
     * Spawns in a temporary {@link FakeEndercrystal} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeEndercrystal} for
     * @param loc  {@link Location} where the {@link FakeEndercrystal} should be spawned at
     * @param name of the {@link FakeEndercrystal} inside the file and above his head
     * @return the new spawned {@link FakeEndercrystal}
     */
    public static FakeEndercrystal spawnTemporaryEndercrystal(Player p, Location loc, String name) {
        try {
            Object endercrystal = entityEndercrystal.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(endercrystal, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            ReflectionUtil.sendPacket(p, getPacketPlayOutSpawnEntity().newInstance(endercrystal, 51));

            FakeEndercrystal fE = new FakeEndercrystal(loc, name, endercrystal);

            FakeAPI.addFakeEndercrystal(p, fE);
            return fE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Teleport a {@link FakeEndercrystal} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakeEndercrystal} for
     * @param to            the {@link Location} where the {@link FakeEndercrystal} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param endercrystal  the {@link FakeEndercrystal} which should be teleported
     */
    public static void splitTeleportBigItem(final Player p, final Location to, final int teleportCount, final long wait, final FakeEndercrystal endercrystal) {
        try {
            final Location currentLocation = endercrystal.getCurrentlocation();
            Vector between = to.toVector().subtract(currentLocation.toVector());

            final double toMoveInX = between.getX() / teleportCount;
            final double toMoveInY = between.getY() / teleportCount;
            final double toMoveInZ = between.getZ() / teleportCount;

            splitMap.put(p.getName(), new BukkitRunnable() {
                public void run() {
                    if (!LocationUtil.isSameLocation(currentLocation, to)) {
                        teleportEndercrystal(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), endercrystal);
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
     * Teleports a {@link FakeEndercrystal} to a specific {@link Location} for the given {@link Player}
     *
     * @param p            the {@link Player} to teleport the {@link FakeEndercrystal} for
     * @param loc          the {@link Location} to teleport the {@link FakeEndercrystal} to
     * @param endercrystal the {@link FakeEndercrystal} which should be teleported
     */
    public static void teleportEndercrystal(Player p, Location loc, FakeEndercrystal endercrystal) {
        try {
            if (VERSION != MinecraftVersion.EIGHT) {

                Field x = ReflectionUtil.getNmsClass("Entity").getField("locX"), y = ReflectionUtil.getNmsClass("Entity").getField("locY"), z = ReflectionUtil.getNmsClass("Entity").getField("locZ"), yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw"), pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
                Object a = endercrystal.getNmsEntity();

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
                        ReflectionUtil.getEntityID(endercrystal.getNmsEntity()),
                        FakeAPI.floor(loc.getBlockX() * 32.0D),
                        FakeAPI.floor(loc.getBlockY() * 32.0D),
                        FakeAPI.floor(loc.getBlockZ() * 32.0D),
                        (byte) ((int) (loc.getYaw() * 256.0F / 360.0F)),
                        (byte) ((int) (loc.getPitch() * 256.0F / 360.0F)),
                        true));
            }

            endercrystal.setCurrentlocation(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a {@link FakeEndercrystal} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p            the {@link Player} to destroy the {@link FakeEndercrystal} for
     * @param endercrystal the {@link FakeEndercrystal} to remove
     */
    public static void destroyEndercrystal(Player p, FakeEndercrystal endercrystal) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(endercrystal.getNmsEntity())}));
            FakeAPI.removeFakeEndercrystal(p, endercrystal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakeEndercrystal} for the {@link Player}
     *
     * @param p            the {@link Player} to see the new name of the {@link FakeEndercrystal}
     * @param name         the actual new name of the {@link FakeEndercrystal}
     * @param endercrystal the {@link FakeEndercrystal} to change the name for
     */
    public static void setEndercrystalname(Player p, String name, FakeEndercrystal endercrystal) {
        try {
            setCustomName().invoke(endercrystal.getNmsEntity(), name.replace("&", "§").replace("_", " "));
            setCustomNameVisible().invoke(endercrystal.getNmsEntity(), true);

            Object dw = getDataWatcher().invoke(endercrystal.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(endercrystal.getNmsEntity()), dw, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
