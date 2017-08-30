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
import de.alphahelix.alphalibary.fakeapi.instances.FakeItem;
import de.alphahelix.alphalibary.nms.packets.PPOEntityDestroy;
import de.alphahelix.alphalibary.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.nms.packets.PPOSpawnEntity;
import de.alphahelix.alphalibary.nms.wrappers.EntityItemWrapper;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemFakeUtil {

    private static ReflectionUtil.SaveConstructor entityItem =
            ReflectionUtil.getDeclaredConstructor("EntityItem", ReflectionUtil.getNmsClass("World"),
                    double.class, double.class, double.class, ReflectionUtil.getNmsClass("ItemStack"));

    /**
     * Spawns in a {@link FakeItem} for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeItem} for
     * @param loc  {@link Location} where the {@link FakeItem} should be spawned at
     * @param name of the {@link FakeItem} inside the file and above his head
     * @param type the {@link Material} which should be spawned
     * @return the new spawned {@link FakeItem}
     */
    public static FakeItem spawnItem(Player p, Location loc, String name, Material type) {
        FakeItem fI = spawnTemporaryItem(p, loc, name, type);

        FakeRegister.getItemLocationsFile().addItemToFile(fI);
        return fI;
    }

    /**
     * Spawns in a temporary {@link FakeItem} (disappears after rejoin) for the {@link Player}
     *
     * @param p    the {@link Player} to spawn the {@link FakeItem} for
     * @param loc  {@link Location} where the {@link FakeItem} should be spawned at
     * @param name of the {@link FakeItem} inside the file and above his head
     * @param type the {@link Material} which should be spawned
     * @return the new spawned {@link FakeItem}
     */
    public static FakeItem spawnTemporaryItem(Player p, Location loc, String name, Material type) {
        Object item = entityItem.newInstance(false,
                ReflectionUtil.getWorldServer(p.getWorld()),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                ReflectionUtil.getNMSItemStack(new ItemStack(type)));
        EntityItemWrapper i = new EntityItemWrapper(item);

        i.setItemStack(new ItemStack(type));

        ReflectionUtil.sendPacket(p, new PPOSpawnEntity(item, 2, 0));
        ReflectionUtil.sendPacket(p, new PPOEntityMetadata(i.getEntityID(), i.getDataWatcher()));

        FakeItem fI = new FakeItem(loc, name, item, type);

        FakeAPI.addFakeItem(p, fI);
        return fI;
    }

    /**
     * Removes a {@link FakeItem} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p    the {@link Player} to destroy the {@link FakeItem} for
     * @param item the {@link FakeItem} to remove
     */
    public static void destroyItem(Player p, FakeItem item) {
        ReflectionUtil.sendPacket(p, new PPOEntityDestroy(ReflectionUtil.getEntityID(item.getNmsEntity())));
        FakeAPI.removeFakeItem(p, item);
    }

    /**
     * Sets a new name for the {@link FakeItem} for the {@link Player}
     *
     * @param p    the {@link Player} to see the new name of the {@link FakeItem}
     * @param name the actual new name of the {@link FakeItem}
     * @param item the {@link FakeItem} to change the name for
     */
    public static void setItemname(Player p, String name, FakeItem item) {
        EntityItemWrapper e = new EntityItemWrapper(item.getNmsEntity());

        e.setCustomName(name.replace("&", "§").replace("_", " "));
        e.setCustomNameVisible(true);

        ReflectionUtil.sendPacket(p, new PPOEntityMetadata(e.getEntityID(), e.getDataWatcher()));
    }

    /**
     * Experimental feature
     */
    public static void setGravity(Player p, boolean gravity, FakeItem item) {
        EntityItemWrapper e = new EntityItemWrapper(item.getNmsEntity());

        e.setNoGravity(!gravity);

        ReflectionUtil.sendPacket(p, new PPOEntityMetadata(e.getEntityID(), e.getDataWatcher()));
    }
}
