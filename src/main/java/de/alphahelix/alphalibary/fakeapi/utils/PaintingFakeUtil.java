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

import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakePainting;
import de.alphahelix.alphalibary.fakeapi.utils.intern.FakeUtilBase;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class PaintingFakeUtil extends FakeUtilBase {

    private static Constructor<?> entityPainting;
    private static Constructor<?> spawnPainting;

    static {
        try {
            entityPainting = ReflectionUtil.getNmsClass("EntityPainting").getConstructor(ReflectionUtil.getNmsClass("World"));
            spawnPainting = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityPainting").getConstructor(ReflectionUtil.getNmsClass("EntityPainting"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns in a {@link FakePainting} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakePainting} for
     * @param loc  {@link Location} where the {@link FakePainting} should be spawned at
     * @param name of the {@link FakePainting} inside the file and above his head
     * @return the new spawned {@link FakePainting}
     */
    public static FakePainting spawnPainting(Player p, Location loc, String name) {
        try {
            Object painting = entityPainting.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(painting, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            ReflectionUtil.sendPacket(p, spawnPainting.newInstance(painting));

            FakeRegister.getPaintingLocationsFile().addPaintingToFile(loc, name);
            FakeAPI.addFakePainting(p, new FakePainting(loc, name, painting));
            return new FakePainting(loc, name, painting);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Spawns in a temporary {@link FakePainting} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakePainting} for
     * @param loc  {@link Location} where the {@link FakePainting} should be spawned at
     * @param name of the {@link FakePainting} inside the file and above his head
     * @return the new spawned {@link FakePainting}
     */
    public static FakePainting spawnTemporaryPainting(Player p, Location loc, String name) {
        try {
            Object painting = entityPainting.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(painting, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            ReflectionUtil.sendPacket(p, spawnPainting.newInstance(painting));

            FakeAPI.addFakePainting(p, new FakePainting(loc, name, painting));
            return new FakePainting(loc, name, painting);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes a {@link FakePainting} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p        the {@link Player} to destroy the {@link FakePainting} for
     * @param painting the {@link FakePainting} to remove
     */
    public static void destroyPainting(Player p, FakePainting painting) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(painting.getNmsEntity())}));
            FakeAPI.removeFakePainting(p, painting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakePainting} for the {@link Player}
     *
     * @param p        the {@link Player} to see the new name of the {@link FakePainting}
     * @param name     the actual new name of the {@link FakePainting}
     * @param painting the {@link FakePainting} to change the name for
     */
    public static void setPaintingname(Player p, String name, FakePainting painting) {
        try {
            setCustomName().invoke(painting.getNmsEntity(), name.replace("&", "§").replace("_", " "));
            setCustomNameVisible().invoke(painting.getNmsEntity(), true);

            Object dw = getDataWatcher().invoke(painting.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(painting.getNmsEntity()), dw, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
