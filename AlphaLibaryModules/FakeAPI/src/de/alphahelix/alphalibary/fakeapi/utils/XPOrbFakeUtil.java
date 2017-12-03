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
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityDestroy;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class XPOrbFakeUtil {

    private static final ReflectionUtil.SaveConstructor ENTITY_XP_ORB =
            ReflectionUtil.getDeclaredConstructor("EntityExperienceOrb", ReflectionUtil.getNmsClass("World"));
    private static final ReflectionUtil.SaveConstructor SPAWN_XP_ORB =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutSpawnEntityExperienceOrb", ReflectionUtil.getNmsClass("EntityExperienceOrb"));

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

        FakeAPI.addFakeEntity(p, fXO);
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
        Object orb = ENTITY_XP_ORB.newInstance(false, ReflectionUtil.getWorldServer(p.getWorld()));
        EntityWrapper o = new EntityWrapper(orb);

        o.setLocation(loc);

        ReflectionUtil.sendPacket(p, SPAWN_XP_ORB.newInstance(false, orb));

        FakeXPOrb fXO = new FakeXPOrb(loc, name, orb);

        FakeAPI.addFakeEntity(p, fXO);
        return fXO;
    }

    /**
     * Removes a {@link FakeXPOrb} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p   the {@link Player} to destroy the {@link FakeXPOrb} for
     * @param orb the {@link FakeXPOrb} to remove
     */
    public static void destroyOrb(Player p, FakeXPOrb orb) {
        ReflectionUtil.sendPacket(p, new PPOEntityDestroy(ReflectionUtil.getEntityID(orb.getNmsEntity())));
        FakeAPI.removeFakeEntity(p, orb);
    }

    /**
     * Sets a new name for the {@link FakeXPOrb} for the {@link Player}
     *
     * @param p    the {@link Player} to see the new name of the {@link FakeXPOrb}
     * @param name the actual new name of the {@link FakeXPOrb}
     * @param orb  the {@link FakeXPOrb} to change the name for
     */
    public static void setOrbname(Player p, String name, FakeXPOrb orb) {
        EntityWrapper o = new EntityWrapper(orb.getNmsEntity());

        o.setCustomName(name.replace("&", "§").replace("_", " "));
        o.setCustomNameVisible(true);

        ReflectionUtil.sendPacket(p, new PPOEntityMetadata(o.getEntityID(), o.getDataWatcher()));
    }
}
