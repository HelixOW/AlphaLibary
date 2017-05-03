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
import de.alphahelix.alphalibary.fakeapi.instances.FakeXPOrb;
import de.alphahelix.alphalibary.fakeapi.utils.intern.FakeUtilBase;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class XPOrbFakeUtil extends FakeUtilBase {

    private static Constructor<?> entityXPOrb;
    private static Constructor<?> spawnXPOrb;

    static {
        try {
            entityXPOrb = ReflectionUtil.getNmsClass("EntityExperienceOrb").getConstructor(ReflectionUtil.getNmsClass("World"));
            spawnXPOrb = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityExperienceOrb").getConstructor(ReflectionUtil.getNmsClass("EntityExperienceOrb"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spawns in a {@link FakeXPOrb} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeXPOrb} for
     * @param loc  {@link Location} where the {@link FakeXPOrb} should be spawned at
     * @param name of the {@link FakeXPOrb} inside the file and above his head
     * @return the new spawned {@link FakeXPOrb}
     */
    public static FakeXPOrb spawnXPOrb(Player p, Location loc, String name) {
        FakeXPOrb fXO = spawnTemporaryXPOrb(p, loc, name);

        FakeRegister.getXpOrbLocationsFile().addXPOrbToFile(fXO);

        FakeAPI.addFakeXPOrb(p, fXO);
        return fXO;
    }

    /**
     * Spawns in a temporary {@link FakeXPOrb} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeXPOrb} for
     * @param loc  {@link Location} where the {@link FakeXPOrb} should be spawned at
     * @param name of the {@link FakeXPOrb} inside the file and above his head
     * @return the new spawned {@link FakeXPOrb}
     */
    public static FakeXPOrb spawnTemporaryXPOrb(Player p, Location loc, String name) {
        try {
            Object orb = entityXPOrb.newInstance(ReflectionUtil.getWorldServer(p.getWorld()));

            setLocation().invoke(orb, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

            ReflectionUtil.sendPacket(p, spawnXPOrb.newInstance(orb));

            FakeXPOrb fXO = new FakeXPOrb(loc, name, orb);

            FakeAPI.addFakeXPOrb(p, fXO);
            return fXO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes a {@link FakeXPOrb} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p   the {@link Player} to destroy the {@link FakeXPOrb} for
     * @param orb the {@link FakeXPOrb} to remove
     */
    public static void destroyOrb(Player p, FakeXPOrb orb) {
        try {
            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityDestroy().newInstance(new int[]{ReflectionUtil.getEntityID(orb.getNmsEntity())}));
            FakeAPI.removeFakeXPOrb(p, orb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new name for the {@link FakeXPOrb} for the {@link Player}
     *
     * @param p    the {@link Player} to see the new name of the {@link FakeXPOrb}
     * @param name the actual new name of the {@link FakeXPOrb}
     * @param orb  the {@link FakeXPOrb} to change the name for
     */
    public static void setOrbname(Player p, String name, FakeXPOrb orb) {
        try {
            setCustomName().invoke(orb.getNmsEntity(), name.replace("&", "§").replace("_", " "));
            setCustomNameVisible().invoke(orb.getNmsEntity(), true);

            Object dw = getDataWatcher().invoke(orb.getNmsEntity());

            ReflectionUtil.sendPacket(p, getPacketPlayOutEntityMetadata().newInstance(ReflectionUtil.getEntityID(orb.getNmsEntity()), dw, true));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
